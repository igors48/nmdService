package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServlet extends HttpServlet {

    // GET /feeds -- feeds list
    // GET /feeds/{feedId} -- get feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final Response responseBody;

            if (pathInfoIsEmpty(pathInfo)) {
                responseBody = getFeedHeaders();
            } else {
                final UUID feedId = parseFeedId(pathInfo);
                responseBody = getFeed(feedId);
            }

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            writeException(exception, response);
        }
    }

    // POST /feeds -- add feed
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String feedUrl = readRequestBody(request);
            final Response responseBody = addFeed(feedUrl);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            writeException(exception, response);
        }
    }

    // DELETE /feeds/{feedId} -- delete feed
    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final UUID feedId = parseFeedId(request.getPathInfo());

            final Response responseBody = ControlServiceWrapper.removeFeed(feedId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            writeException(exception, response);
        }
    }

}
