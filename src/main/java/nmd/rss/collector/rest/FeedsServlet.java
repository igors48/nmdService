package nmd.rss.collector.rest;

import nmd.rss.collector.rest.requests.AddFeedRequest;
import nmd.rss.reader.Category;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.rest.FeedsServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.*;
import static nmd.rss.collector.util.Parameter.isValidUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends AbstractRestServlet {

    // GET -- feed headers list
    // GET /{feedId} -- feed header
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return getFeedHeaders();
        }

        final UUID feedId = parseFeedId(pathInfo);

        return feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : getFeedHeader(feedId);
    }

    // POST -- add feed
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String requestBody = readRequestBody(request);
        final AddFeedRequest addFeedRequest = convert(requestBody);

        if (!isValidUrl(addFeedRequest.feedUrl)) {
            return createErrorJsonResponse(invalidFeedUrl(addFeedRequest.feedUrl));
        }

        if (!Category.isValidCategoryId(addFeedRequest.categoryId)) {
            return createErrorJsonResponse(invalidCategoryId(addFeedRequest.categoryId));
        }
        return addFeed(addFeedRequest.feedUrl, addFeedRequest.categoryId);
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
