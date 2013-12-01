package nmd.rss.collector.rest;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.ControlService;
import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.exporter.FeedExporter;
import nmd.rss.collector.exporter.FeedExporterException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository;
import nmd.rss.collector.gae.persistence.GaeFeedItemsRepository;
import nmd.rss.collector.gae.persistence.GaeFeedUpdateTaskRepository;
import nmd.rss.collector.gae.persistence.GaeRootRepository;
import nmd.rss.collector.gae.updater.GaeCacheFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.rest.responses.*;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.ReadFeedItemsRepository;
import nmd.rss.reader.gae.GaeReadFeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ResponseBody.createJsonResponse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(ControlServiceWrapper.class.getName());

    public static ResponseBody addFeed(final String feedUrl) {
        //TODO feedUrl can be null. need to check it
        final ControlService controlService = createControlService();

        try {
            final UUID feedId = controlService.addFeed(feedUrl);

            final FeedIdResponse feedIdResponse = FeedIdResponse.create(feedId);

            LOGGER.info(String.format("Feed [ %s ] added. Id is [ %s ]", feedUrl, feedId));

            return createJsonResponse(feedIdResponse);
        } catch (ControlServiceException exception) {
            LOGGER.log(Level.SEVERE, String.format("Error adding feed [ %s ]", feedUrl), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody removeFeed(final UUID feedId) {
        //TODO feedId can be null. need to check it
        final ControlService controlService = createControlService();

        controlService.removeFeed(feedId);

        final SuccessMessageResponse successMessageResponse = SuccessMessageResponse.create(String.format("Feed [ %s ] removed", feedId));

        LOGGER.info(String.format("Feed [ %s ] removed", feedId));

        return createJsonResponse(successMessageResponse);
    }

    public static ResponseBody getFeedHeaders() {
        final ControlService controlService = createControlService();

        final List<FeedHeader> headers = controlService.getFeedHeaders();
        final FeedHeadersResponse feedHeadersResponse = FeedHeadersResponse.convert(headers);

        LOGGER.info(String.format("[ %s ] feed headers found", headers.size()));

        return createJsonResponse(feedHeadersResponse);
    }

    public static ResponseBody getFeed(final UUID feedId) {
        final ControlService controlService = createControlService();

        try {
            final Feed feed = controlService.getFeed(feedId);
            final String feedAsXml = FeedExporter.export(feed.header, feed.items);

            LOGGER.info(String.format("Feed [ %s ] link [ %s ] items exported. Items count [ %d ]", feedId, feed.header.feedLink, feed.items.size()));

            return new ResponseBody(ContentType.XML, feedAsXml);
        } catch (ControlServiceException exception) {
            LOGGER.log(Level.SEVERE, String.format("Error export feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(exception);
        } catch (FeedExporterException exception) {
            LOGGER.log(Level.SEVERE, String.format("Error export feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(ServiceError.feedExportError(feedId));
        }
    }

    public static ResponseBody updateCurrentFeed() {
        final ControlService controlService = createControlService();

        try {
            final FeedUpdateReport report = controlService.updateCurrentFeed();
            final FeedMergeReportResponse response = FeedMergeReportResponse.convert(report);

            LOGGER.info(String.format("Feed with id [ %s ] link [ %s ] updated. Added [ %d ] retained [ %d ] removed [ %d ] items", report.feedId, report.feedLink, report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size()));

            return createJsonResponse(response);
        } catch (ControlServiceException exception) {
            LOGGER.log(Level.SEVERE, "Error update current feed ", exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody updateFeed(final UUID feedId) {
        //TODO feedId can be null. need to check it
        final ControlService controlService = createControlService();

        try {
            final FeedUpdateReport report = controlService.updateFeed(feedId);
            final FeedMergeReportResponse response = FeedMergeReportResponse.convert(report);

            LOGGER.info(String.format("Feed with id [ %s ] link [ %s ] updated. Added [ %d ] retained [ %d ] removed [ %d ] items", report.feedId, report.feedLink, report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size()));

            return createJsonResponse(response);
        } catch (ControlServiceException exception) {
            LOGGER.log(Level.SEVERE, String.format("Error update feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody getFeedsReadReport() {
        final ControlService controlService = createControlService();

        final List<FeedReadReport> feedReadReport = controlService.getFeedsReadReport();
        final FeedReadReportsResponse response = FeedReadReportsResponse.convert(feedReadReport);

        LOGGER.info("Feed read report created");

        return createJsonResponse(response);
    }

    public static ResponseBody clear() {
        final ControlService controlService = createControlService();

        controlService.clear();

        final SuccessMessageResponse successMessageResponse = SuccessMessageResponse.create("Service cleared");

        return createJsonResponse(successMessageResponse);
    }

    public static ResponseBody getLatestNotReadItem(final UUID feedId) {
        //TODO feedId can be null. need to check it
        final ControlService controlService = createControlService();

        final FeedItem latestNotReadItem = controlService.getLatestNotReadItem(feedId);
        //TODO latestNotReadItem can be null
        final FeedItemResponse response = FeedItemResponse.convert(latestNotReadItem);

        LOGGER.info(String.format("Latest item with link [ %s ] and id [ %s ] from feed id [ %s ] retrieved", latestNotReadItem.link, latestNotReadItem.guid, feedId.toString()));

        return createJsonResponse(response);
    }

    public static ResponseBody markItemAsRead(final UUID feedId, final String itemId) {
        //TODO feedId can be null. need to check it
        //TODO itemId can be null. need to check it
        final ControlService controlService = createControlService();

        controlService.markItemAsRead(feedId, itemId);

        final SuccessMessageResponse successMessageResponse = SuccessMessageResponse.create(String.format("Item [ %s ] from feed [ %s ] marked as read", itemId, feedId));

        return createJsonResponse(successMessageResponse);
    }

    //TODO consider lazy init
    private static ControlService createControlService() {
        final Transactions transactions = new GaeRootRepository();
        final UrlFetcher urlFetcher = new GaeUrlFetcher();

        final FeedUpdateTaskRepository feedUpdateTaskRepository = new GaeFeedUpdateTaskRepository();
        final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository();
        final FeedHeadersRepository feedHeadersRepository = new GaeFeedHeadersRepository();
        final ReadFeedItemsRepository readFeedItemsRepository = new GaeReadFeedItemsRepository();
        final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository = new GaeCacheFeedUpdateTaskSchedulerContextRepository();

        final FeedUpdateTaskScheduler feedUpdateTaskScheduler = new CycleFeedUpdateTaskScheduler(feedUpdateTaskSchedulerContextRepository, feedUpdateTaskRepository, transactions);

        return new ControlService(feedHeadersRepository, feedItemsRepository, feedUpdateTaskRepository, readFeedItemsRepository, feedUpdateTaskSchedulerContextRepository, feedUpdateTaskScheduler, urlFetcher, transactions);
    }

}
