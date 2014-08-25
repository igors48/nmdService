package nmd.orb.http.servlets.feeds;

import nmd.orb.http.Handler;
import nmd.orb.http.requests.AddFeedRequest;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.FeedsServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.collector.error.ServiceError.invalidCategoryId;
import static nmd.orb.collector.error.ServiceError.invalidFeedUrl;
import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.isValidUrl;
import static nmd.orb.collector.util.Parameter.notNull;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ServletTools.convert;
import static nmd.orb.reader.Category.isValidCategoryId;

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
