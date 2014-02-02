package nmd.rss.collector.rest;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.controller.UpdatesService;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository;
import nmd.rss.collector.gae.persistence.GaeFeedItemsRepository;
import nmd.rss.collector.gae.persistence.GaeFeedUpdateTaskRepository;
import nmd.rss.collector.gae.persistence.GaeRootRepository;
import nmd.rss.collector.gae.updater.GaeCacheFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ResponseBody.createJsonResponse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class UpdatesServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(UpdatesServiceWrapper.class.getName());
    private static final UpdatesService UPDATES_SERVICE = createUpdatesService();

    public static ResponseBody updateCurrentFeed() {

        try {
            final FeedUpdateReport report = UPDATES_SERVICE.updateCurrentFeed();
            final FeedMergeReportResponse response = FeedMergeReportResponse.convert(report);

            LOGGER.info(format("Feed with id [ %s ] link [ %s ] updated. Added [ %d ] retained [ %d ] removed [ %d ] items", report.feedId, report.feedLink, report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size()));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            final ServiceError serviceError = exception.getError();

            LOGGER.log(Level.SEVERE, format("Error update current feed [ %s ]", serviceError), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody updateFeed(final UUID feedId) {

        try {
            final FeedUpdateReport report = UPDATES_SERVICE.updateFeed(feedId);
            final FeedMergeReportResponse response = FeedMergeReportResponse.convert(report);

            LOGGER.info(format("Feed with id [ %s ] link [ %s ] updated. Added [ %d ] retained [ %d ] removed [ %d ] items", report.feedId, report.feedLink, report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size()));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error update feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    private static UpdatesService createUpdatesService() {
        final Transactions transactions = new GaeRootRepository();
        final UrlFetcher urlFetcher = new GaeUrlFetcher();

        final FeedUpdateTaskRepository feedUpdateTaskRepository = new GaeFeedUpdateTaskRepository();
        final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository();
        final FeedHeadersRepository feedHeadersRepository = new GaeFeedHeadersRepository();
        final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository = new GaeCacheFeedUpdateTaskSchedulerContextRepository();

        final FeedUpdateTaskScheduler feedUpdateTaskScheduler = new CycleFeedUpdateTaskScheduler(feedUpdateTaskSchedulerContextRepository, feedUpdateTaskRepository, transactions);

        return new UpdatesService(feedHeadersRepository, feedItemsRepository, feedUpdateTaskRepository, feedUpdateTaskScheduler, urlFetcher, transactions);
    }

}
