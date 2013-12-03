package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FeedsServlet.class.getName());

    // GET -- feeds list
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final ResponseBody responseBody = getFeedHeaders();

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

    // POST -- add feed
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String feedUrl = readRequestBody(request);
            final ResponseBody responseBody = addFeed(feedUrl);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

    // DELETE /{feedId} -- delete feed
    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final UUID feedId = parseFeedId(pathInfo);

            final ResponseBody responseBody = feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : removeFeed(feedId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
