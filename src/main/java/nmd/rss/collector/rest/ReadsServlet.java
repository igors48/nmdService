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
 * Date : 27.11.13
 */
public class ReadsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReadsServlet.class.getName());

    //GET get -- reads report
    //GET/{feedId} -- get latest not read item
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final String pathInfo = request.getPathInfo();

            final ResponseBody responseBody;

            if (pathInfoIsEmpty(pathInfo)) {
                responseBody = getFeedsReadReport();
            } else {
                final UUID feedId = parseFeedId(pathInfo);
                //TODO feedId can be null
                responseBody = getLatestNotReadItem(feedId);
            }

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
            //TODO feedAndItemIds can be null
            final ResponseBody responseBody = markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId);

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
