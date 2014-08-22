package nmd.rss.collector.rest.servlets.feeds;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.error.ServiceError.invalidFeedTitle;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseFeedId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class FeedsServletPutRequestHandler implements Handler {

    public static final FeedsServletPutRequestHandler FEEDS_SERVLET_PUT_REQUEST_HANDLER = new FeedsServletPutRequestHandler(FeedsServiceWrapper.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapper feedsService;

    public FeedsServletPutRequestHandler(final FeedsServiceWrapper feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // PUT /{feedId} -- update feed title
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

        if (feedId == null) {
            return createErrorJsonResponse(invalidFeedId(element));
        }

        return isValidFeedHeaderTitle(body) ? this.feedsService.updateFeedTitle(feedId, body) : createErrorJsonResponse(invalidFeedTitle(body));
    }

}