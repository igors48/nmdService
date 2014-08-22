package nmd.rss.http.wrappers;

import nmd.rss.collector.controller.*;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.gae.GaeServices;
import nmd.rss.http.responses.FeedMergeReportResponse;
import nmd.rss.http.responses.FeedSeriesUpdateResponse;
import nmd.rss.http.tools.ResponseBody;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.http.responses.FeedMergeReportResponse.create;
import static nmd.rss.http.responses.FeedSeriesUpdateResponse.convert;
import static nmd.rss.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.http.tools.ResponseBody.createJsonResponse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class UpdatesServiceWrapperImpl implements UpdatesServiceWrapper {

    public static final UpdatesServiceWrapperImpl UPDATES_SERVICE_WRAPPER = new UpdatesServiceWrapperImpl(GaeServices.UPDATES_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(UpdatesServiceWrapperImpl.class.getName());

    private static final long UPDATE_PERIOD = 9000;

    private final UpdatesService updatesService;

    public UpdatesServiceWrapperImpl(final UpdatesService updatesService) {
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
