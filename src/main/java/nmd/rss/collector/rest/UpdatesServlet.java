package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static nmd.rss.collector.rest.ControlServiceWrapper.updateCurrentFeed;
import static nmd.rss.collector.rest.ControlServiceWrapper.updateFeed;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class UpdatesServlet extends HttpServlet {

    // GET -- update current feed
    // GET /{feedId} -- update feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final ResponseBody responseBody;

            if (pathInfoIsEmpty(pathInfo)) {
                responseBody = updateCurrentFeed();
            } else {
                final UUID feedId = parseFeedId(pathInfo);
                responseBody = updateFeed(feedId);
            }

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            writeException(exception, response);
        }
    }

}
