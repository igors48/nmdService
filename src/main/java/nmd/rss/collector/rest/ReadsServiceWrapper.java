package nmd.rss.collector.rest;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.controller.ReadsService;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository;
import nmd.rss.collector.gae.persistence.GaeFeedItemsRepository;
import nmd.rss.collector.gae.persistence.GaeRootRepository;
import nmd.rss.collector.rest.responses.FeedItemsReportResponse;
import nmd.rss.collector.rest.responses.FeedReadReportsResponse;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.ReadFeedItemsRepository;
import nmd.rss.reader.gae.GaeReadFeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ResponseBody.createJsonResponse;
import static nmd.rss.collector.rest.responses.FeedItemsReportResponse.convert;
import static nmd.rss.collector.rest.responses.SuccessMessageResponse.create;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ReadsServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(ReadsServiceWrapper.class.getName());

    private static final ReadsService READS_SERVICE = createReadsService();

    public static ResponseBody getFeedsReadReport() {
        final List<FeedReadReport> feedReadReport = READS_SERVICE.getFeedsReadReport();
        final FeedReadReportsResponse response = FeedReadReportsResponse.convert(feedReadReport);

        LOGGER.info("Feed read report created");

        return createJsonResponse(response);
    }

    public static ResponseBody markItemAsRead(final UUID feedId, final String itemId) {

        try {
            READS_SERVICE.markItemAsRead(feedId, itemId);

            final String message = format("Item [ %s ] from feed [ %s ] marked as read", itemId, feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error mark feed [ %s ] item [ %s ] as read", feedId, itemId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody markAllItemsAsRead(final UUID feedId) {

        try {
            READS_SERVICE.markAllItemsAsRead(feedId);

            final String message = format("All feed [ %s ] items marked as read", feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error mark feed [ %s ] items as read", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody toggleItemAsReadLater(final UUID feedId, final String itemId) {

        try {
            READS_SERVICE.toggleReadLaterItemMark(feedId, itemId);

            final String message = format("Item [ %s ] from feed [ %s ] toggled as read later", itemId, feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error toggle feed [ %s ] item [ %s ] as read later", feedId, itemId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody getFeedItemsReport(final UUID feedId) {

        try {
            FeedItemsReport report = READS_SERVICE.getFeedItemsReport(feedId);
            FeedItemsReportResponse response = convert(report);

            LOGGER.info(format("Feed [ %s ] items report created", feedId));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error getting feed [ %s ] items report ", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    private static ReadsService createReadsService() {
        final Transactions transactions = new GaeRootRepository();
        final UrlFetcher urlFetcher = new GaeUrlFetcher();

        final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository();
        final FeedHeadersRepository feedHeadersRepository = new GaeFeedHeadersRepository();
        final ReadFeedItemsRepository readFeedItemsRepository = new GaeReadFeedItemsRepository();

        return new ReadsService(feedHeadersRepository, feedItemsRepository, readFeedItemsRepository, urlFetcher, transactions);
    }

}
