package nmd.rss.collector.rest.wrappers;

import nmd.rss.collector.controller.*;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import nmd.rss.collector.rest.responses.FeedSeriesUpdateResponse;
import nmd.rss.collector.rest.tools.ResponseBody;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.rest.responses.FeedMergeReportResponse.create;
import static nmd.rss.collector.rest.responses.FeedSeriesUpdateResponse.convert;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ResponseBody.createJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class UpdatesServiceWrapper implements UpdatesServiceInterface {

    private static final Logger LOGGER = Logger.getLogger(UpdatesServiceWrapper.class.getName());

    private static final long UPDATE_PERIOD = 9000;

    private final UpdatesService updatesService;

    public UpdatesServiceWrapper(final UpdatesService updatesService) {
        guard(notNull(updatesService));
        this.updatesService = updatesService;
    }

    @Override
    public ResponseBody updateCurrentFeeds() {
        final Quota quota = new TimeQuota(UPDATE_PERIOD);
        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(quota);
        final FeedSeriesUpdateResponse response = convert(report);

        return createJsonResponse(response);
    }

    @Override
    public ResponseBody updateFeed(final UUID feedId) {

        try {
            final FeedUpdateReport report = this.updatesService.updateFeed(feedId);
            final FeedMergeReportResponse response = create(report);

            LOGGER.info(format("Feed with id [ %s ] link [ %s ] updated. Added [ %d ] retained [ %d ] removed [ %d ] items", report.feedId, report.feedLink, report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size()));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error update feed [ %s ]", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

}
