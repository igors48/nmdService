package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.AbstractRestServlet;
import nmd.rss.collector.rest.tools.FeedAndItemIds;
import nmd.rss.collector.rest.tools.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.*;
import static nmd.rss.collector.rest.wrappers.ReadsServiceWrapper.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends AbstractRestServlet {

    private static final String MARK_AS_PARAMETER_NAME = "markAs";
    private static final String MARK_AS_READ = "read";
    private static final String MARK_AS_READ_LATER = "readLater";

    //GET -- reads report
    //GET /{feedId} -- feed items report
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return getFeedsReadReport();
        }

        final UUID feedId = parseFeedId(pathInfo);

        return isValidFeedHeaderId(feedId) ? getFeedItemsReport(feedId) : createErrorJsonResponse(invalidFeedId(pathInfo));
    }

    //PUT /{feedId}/{itemId}&mark-as=read|read-later -- mark item as read or read later
    //PUT /{feedId} -- mark all items as read
    @Override
    protected ResponseBody handlePut(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final FeedAndItemIds feedAndItemIds = parseFeedAndItemIds(pathInfo);

        if (feedAndItemIds == null) {
            return createErrorJsonResponse(invalidFeedOrItemId(pathInfo));
        }

        if (feedAndItemIds.itemId.isEmpty()) {
            return markAllItemsAsRead(feedAndItemIds.feedId);
        }

        final String markMode = request.getParameter(MARK_AS_PARAMETER_NAME);

        if (!(MARK_AS_READ.equals(markMode) || MARK_AS_READ_LATER.equals(markMode))) {
            return createErrorJsonResponse(invalidMarkMode(markMode));
        }

        return markMode.equals(MARK_AS_READ) ? markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId) : toggleItemAsReadLater(feedAndItemIds.feedId, feedAndItemIds.itemId);
    }

}
