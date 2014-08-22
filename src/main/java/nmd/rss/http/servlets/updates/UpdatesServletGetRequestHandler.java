package nmd.rss.http.servlets.updates;

import nmd.rss.http.Handler;
import nmd.rss.http.tools.ResponseBody;
import nmd.rss.http.wrappers.UpdatesServiceWrapperImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.http.tools.ServletTools.parseFeedId;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class UpdatesServletGetRequestHandler implements Handler {

    public static final UpdatesServletGetRequestHandler UPDATES_SERVLET_GET_REQUEST_HANDLER = new UpdatesServletGetRequestHandler(UpdatesServiceWrapperImpl.UPDATES_SERVICE_WRAPPER);

    private final UpdatesServiceWrapperImpl updatesService;

    public UpdatesServletGetRequestHandler(final UpdatesServiceWrapperImpl updatesService) {
        guard(notNull(updatesService));
        this.updatesService = updatesService;
    }

    // GET -- update current feed
    // GET /{feedId} -- update feed
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return this.updatesService.updateCurrentFeeds();
        }

        final String pathInfo = elements.get(0);
        final UUID feedId = parseFeedId(pathInfo);

        return isValidFeedHeaderId(feedId) ? this.updatesService.updateFeed(feedId) : createErrorJsonResponse(invalidFeedId(pathInfo));
    }

}
