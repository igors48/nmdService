package nmd.rss.collector.rest;

import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.controller.UpdatesService;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.gae.fetcher.GaeUrlFetcher.GAE_URL_FETCHER;
import static nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository.GAE_FEED_HEADERS_REPOSITORY;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
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

        final FeedUpdateTaskScheduler feedUpdateTaskScheduler = new CycleFeedUpdateTaskScheduler(GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY, GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY, GAE_TRANSACTIONS);

        return new UpdatesService(GAE_FEED_HEADERS_REPOSITORY, GAE_CACHED_FEED_ITEMS_REPOSITORY, GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY, feedUpdateTaskScheduler, GAE_URL_FETCHER, GAE_TRANSACTIONS);
    }

}
