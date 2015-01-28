package nmd.orb.http.servlets.rss;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.FeedsServiceWrapper;

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
public class ExportRssServletGetRequestHandler implements Handler {

    public static final ExportRssServletGetRequestHandler EXPORT_RSS_SERVLET_GET_REQUEST_HANDLER = new ExportRssServletGetRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper());

    private final FeedsServiceWrapper feedsService;

    public ExportRssServletGetRequestHandler(final FeedsServiceWrapper feedsService) {
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
