package nmd.rss.collector.rest.wrappers;

import nmd.rss.collector.controller.FeedItemsCardsReport;
import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.controller.ReadsService;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.gae.GaeServices;
import nmd.rss.collector.rest.responses.FeedItemsCardsReportResponse;
import nmd.rss.collector.rest.responses.FeedItemsReportResponse;
import nmd.rss.collector.rest.responses.FeedReadReportsResponse;
import nmd.rss.collector.rest.tools.ResponseBody;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.rest.responses.FeedItemsReportResponse.convert;
import static nmd.rss.collector.rest.responses.SuccessMessageResponse.create;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ResponseBody.createJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

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
            FeedItemsReport report = this.readsService.getFeedItemsReport(feedId);
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
