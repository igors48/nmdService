package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.AbstractRestServlet;
import nmd.rss.collector.rest.tools.FeedAndItemIds;
import nmd.rss.collector.rest.tools.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.*;
import static nmd.rss.collector.rest.wrappers.ReadsServiceWrapper.*;
import static nmd.rss.collector.util.Parameter.isPositive;

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
    //GET /{feedId}/{offset}/{size} -- feed items cards report
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return getFeedsReadReport();
        }

        final List<String> elements = parse(pathInfo);

        if (elements.size() == 1) {
            final String element = elements.get(0);
            final UUID feedId = parseUuid(element);

            return isValidFeedHeaderId(feedId) ? getFeedItemsReport(feedId) : createErrorJsonResponse(invalidFeedId(pathInfo));
        }

        final String feedIdAsString = elements.size() > 1 ? elements.get(0) : "";
        final String offsetAsString = elements.size() > 1 ? elements.get(1) : "";
        final String sizeAsString = elements.size() > 2 ? elements.get(2) : "";

        if (elements.size() == 2) {
            return createErrorJsonResponse(invalidOffsetOrSize(offsetAsString, sizeAsString));
        }

        final Integer offset = parseInteger(offsetAsString);
        final Integer size = parseInteger(sizeAsString);

        if (offset == null || size == null) {
            return createErrorJsonResponse(invalidOffsetOrSize(offsetAsString, sizeAsString));
        }

        if (!(isPositive(offset) && isPositive(size))) {
            return createErrorJsonResponse(invalidOffsetOrSize(offsetAsString, sizeAsString));
        }

        final UUID feedId = parseUuid(feedIdAsString);

        return isValidFeedHeaderId(feedId) ? getFeedItemsCardsReport(feedId, offset, size) : createErrorJsonResponse(invalidFeedId(pathInfo));
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
