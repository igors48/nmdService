package nmd.rss.collector.rest.servlets.feeds;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.requests.AddFeedRequest;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.error.ServiceError.invalidCategoryId;
import static nmd.rss.collector.error.ServiceError.invalidFeedUrl;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.convert;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isValidUrl;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.reader.Category.isValidCategoryId;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class FeedsServletPostRequestHandler implements Handler {

    public static final FeedsServletPostRequestHandler FEEDS_SERVLET_POST_REQUEST_HANDLER = new FeedsServletPostRequestHandler(FeedsServiceWrapperImpl.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapperImpl feedsService;

    public FeedsServletPostRequestHandler(final FeedsServiceWrapperImpl feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // POST -- add feed
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        final AddFeedRequest addFeedRequest = convert(body);

        if (addFeedRequest == null) {
            return createErrorJsonResponse(invalidFeedUrl(""));
        }

        if (!isValidUrl(addFeedRequest.feedUrl)) {
            return createErrorJsonResponse(invalidFeedUrl(addFeedRequest.feedUrl));
        }

        if (!isValidCategoryId(addFeedRequest.categoryId)) {
            return createErrorJsonResponse(invalidCategoryId(addFeedRequest.categoryId));
        }

        return this.feedsService.addFeed(addFeedRequest.feedUrl, addFeedRequest.categoryId);
    }

}
