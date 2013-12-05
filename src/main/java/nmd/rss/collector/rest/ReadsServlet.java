package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.error.ServiceError.invalidFeedOrItemId;
import static nmd.rss.collector.rest.ControlServiceWrapper.getFeedsReadReport;
import static nmd.rss.collector.rest.ControlServiceWrapper.markItemAsRead;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReadsServlet.class.getName());

    //GET get -- reads report
    //GET/{feedId} -- get latest not read item
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final ResponseBody responseBody = getFeedsReadReport();

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

    //POST/{feedId}/{itemId} -- mark item as read
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final FeedAndItemIds feedAndItemIds = parseFeedAndItemIds(pathInfo);

            final ResponseBody responseBody = feedAndItemIds == null ? createErrorJsonResponse(invalidFeedOrItemId(pathInfo)) : markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
