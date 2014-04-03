package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.AbstractRestServlet;
import nmd.rss.collector.rest.tools.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseFeedId;
import static nmd.rss.collector.rest.tools.ServletTools.pathInfoIsEmpty;
import static nmd.rss.collector.rest.wrappers.UpdatesServiceWrapper.updateCurrentFeeds;
import static nmd.rss.collector.rest.wrappers.UpdatesServiceWrapper.updateFeed;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class UpdatesServlet extends AbstractRestServlet {

    // GET -- update current feed
    // GET /{feedId} -- update feed
    @Override
    protected ResponseBody handleGet(HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return updateCurrentFeeds();
        }

        final UUID feedId = parseFeedId(pathInfo);

        return isValidFeedHeaderId(feedId) ? updateFeed(feedId) : createErrorJsonResponse(invalidFeedId(pathInfo));
    }

}
