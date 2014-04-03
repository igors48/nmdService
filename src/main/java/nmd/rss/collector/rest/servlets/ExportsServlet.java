package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.AbstractRestServlet;
import nmd.rss.collector.rest.tools.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseFeedId;
import static nmd.rss.collector.rest.wrappers.FeedsServiceWrapper.getFeed;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportsServlet extends AbstractRestServlet {

    // GET /{feedId} -- get feed
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final UUID feedId = parseFeedId(pathInfo);

        return isValidFeedHeaderId(feedId) ? getFeed(feedId) : createErrorJsonResponse(invalidFeedId(pathInfo));
    }

}
