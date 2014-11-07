package nmd.orb.http.wrappers;

import nmd.orb.error.ServiceException;
import nmd.orb.gae.GaeServices;
import nmd.orb.http.responses.FeedItemsCardsReportResponse;
import nmd.orb.http.responses.FeedItemsReportResponse;
import nmd.orb.http.responses.FeedReadReportsResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.ReadsService;
import nmd.orb.services.report.FeedItemsCardsReport;
import nmd.orb.services.report.FeedItemsReport;
import nmd.orb.services.report.FeedReadReport;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.http.responses.FeedItemsReportResponse.convert;
import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ReadsServiceWrapperImpl implements ReadsServiceWrapper {

    public static final ReadsServiceWrapperImpl READS_SERVICE_WRAPPER = new ReadsServiceWrapperImpl(GaeServices.READS_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(ReadsServiceWrapperImpl.class.getName());

    private final ReadsService readsService;

    public ReadsServiceWrapperImpl(final ReadsService readsService) {
        guard(notNull(readsService));
        this.readsService = readsService;
    }

    @Override
    public ResponseBody getFeedsReadReport() {
        final List<FeedReadReport> feedReadReport = this.readsService.getFeedsReadReport();
        final FeedReadReportsResponse response = FeedReadReportsResponse.convert(feedReadReport);

        LOGGER.info("Feed read report created");

        return createJsonResponse(response);
    }

    @Override
    public ResponseBody markItemAsRead(final UUID feedId, final String itemId) {

        try {
            this.readsService.markItemAsRead(feedId, itemId);

            final String message = format("Item [ %s ] from feed [ %s ] marked as read", itemId, feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error mark feed [ %s ] item [ %s ] as read", feedId, itemId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody markAllItemsAsRead(final UUID feedId) {

        try {
            this.readsService.markAllItemsAsRead(feedId);

            final String message = format("All feed [ %s ] items marked as read", feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error mark feed [ %s ] items as read", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody toggleItemAsReadLater(final UUID feedId, final String itemId) {

        try {
            this.readsService.toggleReadLaterItemMark(feedId, itemId);

            final String message = format("Item [ %s ] from feed [ %s ] toggled as read later", itemId, feedId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error toggle feed [ %s ] item [ %s ] as read later", feedId, itemId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody getFeedItemsReport(final UUID feedId) {

        try {
            FeedItemsReport report = this.readsService.getFeedItemsReport(feedId, false);
            FeedItemsReportResponse response = convert(report);

            LOGGER.info(format("Feed [ %s ] items report created", feedId));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error getting feed [ %s ] items report ", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody getFeedItemsCardsReport(final UUID feedId, final int offset, final int size) {
        try {
            FeedItemsCardsReport report = this.readsService.getFeedItemsCardsReport(feedId, offset, size);
            FeedItemsCardsReportResponse response = FeedItemsCardsReportResponse.convert(report);

            LOGGER.info(format("Feed [ %s ] items cards report created", feedId));

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error getting feed [ %s ] items cards report ", feedId), exception);

            return createErrorJsonResponse(exception);
        }
    }

}
