package nmd.rss.http.servlets.feeds;

import nmd.rss.http.Handler;
import nmd.rss.http.tools.ResponseBody;
import nmd.rss.http.wrappers.FeedsServiceWrapperImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.error.ServiceError.invalidFeedTitle;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.http.tools.ServletTools.parseFeedId;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class FeedsServletPutRequestHandler implements Handler {

    public static final FeedsServletPutRequestHandler FEEDS_SERVLET_PUT_REQUEST_HANDLER = new FeedsServletPutRequestHandler(FeedsServiceWrapperImpl.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapperImpl feedsService;

    public FeedsServletPutRequestHandler(final FeedsServiceWrapperImpl feedsService) {
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