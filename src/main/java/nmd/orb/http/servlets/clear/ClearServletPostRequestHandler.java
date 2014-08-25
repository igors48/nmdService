package nmd.orb.http.servlets.clear;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import nmd.orb.http.wrappers.FeedsServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class ClearServletPostRequestHandler implements Handler {

    public static final ClearServletPostRequestHandler CLEAR_SERVLET_POST_REQUEST_HANDLER = new ClearServletPostRequestHandler(FeedsServiceWrapperImpl.FEEDS_SERVICE_WRAPPER);

    private final FeedsServiceWrapper feedsService;

    public ClearServletPostRequestHandler(final FeedsServiceWrapper feedsService) {
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
