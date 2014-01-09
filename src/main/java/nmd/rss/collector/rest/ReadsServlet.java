package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.error.ServiceError.invalidFeedOrItemId;
import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends AbstractRestServlet {

    //GET -- reads report
    //GET /{feedId} -- feed items report
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return getFeedsReadReport();
        }

        final UUID feedId = parseFeedId(pathInfo);

        return feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : getFeedItemsReport(feedId);
    }

    //POST /{feedId}/{itemId} -- mark item as read
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final FeedAndItemIds feedAndItemIds = parseFeedAndItemIds(pathInfo);

        return feedAndItemIds == null ? createErrorJsonResponse(invalidFeedOrItemId(pathInfo)) : markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId);
    }

}
