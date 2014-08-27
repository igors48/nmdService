package nmd.orb.http.servlets.feeds;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.FeedsServiceWrapperImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.orb.error.ServiceError.invalidFeedId;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ServletTools.parseFeedId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class FeedsServletDeleteRequestHandler implements Handler {

    public static final FeedsServletDeleteRequestHandler FEEDS_SERVLET_DELETE_REQUEST_HANDLER = new FeedsServletDeleteRequestHandler(FeedsServiceWrapperImpl.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapperImpl feedsService;

    public FeedsServletDeleteRequestHandler(final FeedsServiceWrapperImpl feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // DELETE /{feedId} -- delete feed
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidFeedId(""));
        }

        final String element = elements.get(0);
        final UUID feedId = parseFeedId(element);

        return isValidFeedHeaderId(feedId) ? this.feedsService.removeFeed(feedId) : createErrorJsonResponse(invalidFeedId(element));
    }
}
