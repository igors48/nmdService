package nmd.rss.collector.rest.servlets.clear;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.FeedsServiceInterface;
import nmd.rss.collector.rest.wrappers.FeedsServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class ClearServletPostRequestHandler implements Handler {

    public static final ClearServletPostRequestHandler CLEAR_SERVLET_POST_REQUEST_HANDLER = new ClearServletPostRequestHandler(FeedsServiceWrapper.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceInterface feedsService;

    public ClearServletPostRequestHandler(final FeedsServiceInterface feedsService) {
        guard(notNull(feedsService));
        this.feedsService = feedsService;
    }

    // POST -- clear service
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.feedsService.clear();
    }

}
