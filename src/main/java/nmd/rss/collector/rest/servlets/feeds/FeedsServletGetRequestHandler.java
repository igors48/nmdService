package nmd.rss.collector.rest.servlets.feeds;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseFeedId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class FeedsServletGetRequestHandler implements Handler {

    public static final FeedsServletGetRequestHandler FEEDS_SERVLET_GET_REQUEST_HANDLER = new FeedsServletGetRequestHandler(FeedsServiceWrapper.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapper feedsService;

    public FeedsServletGetRequestHandler(final FeedsServiceWrapper feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // GET -- feed headers list
    // GET /{feedId} -- feed header
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return this.feedsService.getFeedHeaders();
        }

        final String element = elements.get(0);
        final UUID feedId = parseFeedId(element);

        return isValidFeedHeaderId(feedId) ? this.feedsService.getFeedHeader(feedId) : createErrorJsonResponse(invalidFeedId(element));
    }

}
