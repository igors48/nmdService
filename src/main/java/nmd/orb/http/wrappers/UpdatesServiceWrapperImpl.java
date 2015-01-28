package nmd.orb.http.wrappers;

import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.FeedMergeReportResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.UpdatesService;
import nmd.orb.services.report.FeedUpdateReport;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.http.responses.FeedMergeReportResponse.create;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class UpdatesServiceWrapperImpl implements UpdatesServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(UpdatesServiceWrapperImpl.class.getName());

    private final UpdatesService updatesService;

    public UpdatesServiceWrapperImpl(final UpdatesService updatesService) {
        guard(notNull(updatesService));
        this.updatesService = updatesService;
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
