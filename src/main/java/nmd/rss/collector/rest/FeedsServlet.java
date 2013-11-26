package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FeedsServlet.class.getName());

    // GET -- feeds list
    // GET /{feedId} -- get feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final ResponseBody responseBody;

            if (pathInfoIsEmpty(pathInfo)) {
                responseBody = getFeedHeaders();
            } else {
                final UUID feedId = parseFeedId(pathInfo);
                responseBody = getFeed(feedId);
            }

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
            final UUID feedId = parseFeedId(request.getPathInfo());

            final ResponseBody responseBody = removeFeed(feedId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
