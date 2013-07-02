package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ControlServiceWrapper.getFeed;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExportsServlet.class.getName());

    // GET /{feedId} -- get feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final UUID feedId = parseFeedId(pathInfo);
            final ResponseBody responseBody = getFeed(feedId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
