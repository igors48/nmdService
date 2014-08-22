package nmd.rss.collector.rest.servlets.exports;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapper;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapperImpl;

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
public class ExportsServletGetRequestHandler implements Handler {

    public static final ExportsServletGetRequestHandler EXPORTS_SERVLET_GET_REQUEST_HANDLER = new ExportsServletGetRequestHandler(FeedsServiceWrapperImpl.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapper feedsService;

    public ExportsServletGetRequestHandler(final FeedsServiceWrapper feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // GET /{feedId} -- get feed
    @Override
    public ResponseBody handle(List<String> elements, Map<String, String> parameters, String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidFeedId(""));
        }

        final String feedIdAsString = elements.get(0);
        final UUID feedId = parseFeedId(feedIdAsString);

        return isValidFeedHeaderId(feedId) ? this.feedsService.getFeed(feedId) : createErrorJsonResponse(invalidFeedId(feedIdAsString));
    }

}
