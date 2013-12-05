package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.parseFeedId;
import static nmd.rss.collector.rest.ServletTools.readRequestBody;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends RestServlet {

    // GET -- feeds list
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        return getFeedHeaders();
    }

    // POST -- add feed
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String feedUrl = readRequestBody(request);

        return addFeed(feedUrl);
    }

    // DELETE /{feedId} -- delete feed
    @Override
    protected ResponseBody handleDelete(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final UUID feedId = parseFeedId(pathInfo);

        return feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : removeFeed(feedId);
    }

}
