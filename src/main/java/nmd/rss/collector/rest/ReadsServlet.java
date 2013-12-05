package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;

import static nmd.rss.collector.error.ServiceError.invalidFeedOrItemId;
import static nmd.rss.collector.rest.ControlServiceWrapper.getFeedsReadReport;
import static nmd.rss.collector.rest.ControlServiceWrapper.markItemAsRead;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.parseFeedAndItemIds;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends RestServlet {

    //GET get -- reads report
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        return getFeedsReadReport();
    }

    //POST/{feedId}/{itemId} -- mark item as read
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final FeedAndItemIds feedAndItemIds = parseFeedAndItemIds(pathInfo);

        return feedAndItemIds == null ? createErrorJsonResponse(invalidFeedOrItemId(pathInfo)) : markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId);
    }

}
