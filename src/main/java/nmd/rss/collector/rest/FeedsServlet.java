package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.rest.ControlServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.parseFeedId;
import static nmd.rss.collector.rest.ServletTools.readRequestBody;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends AbstractRestServlet {

    // GET -- feed headers list
    // GET /{feedId} -- feed headers list
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        return getFeedHeaders();
    }

    // POST -- add feed
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String feedUrl = readRequestBody(request);

        return (feedUrl == null || feedUrl.isEmpty()) ? createErrorJsonResponse(urlFetcherError(feedUrl)) : addFeed(feedUrl);
    }

    // PUT /{feedId} -- update feed title
    @Override
    protected ResponseBody handlePut(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();
        final UUID feedId = parseFeedId(pathInfo);

        if (feedId == null) {
            return createErrorJsonResponse(invalidFeedId(pathInfo));
        }

        final String feedTitle = readRequestBody(request);

        return (feedTitle == null || feedTitle.isEmpty()) ? createErrorJsonResponse(invalidFeedTitle(feedTitle)) : updateFeedTitle(feedId, feedTitle);
    }

    // DELETE /{feedId} -- delete feed
    @Override
    protected ResponseBody handleDelete(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final UUID feedId = parseFeedId(pathInfo);

        return feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : removeFeed(feedId);
    }

}
