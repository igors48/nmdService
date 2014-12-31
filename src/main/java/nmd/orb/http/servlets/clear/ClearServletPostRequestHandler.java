package nmd.orb.http.servlets.clear;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ClearServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.http.wrappers.ClearServiceWrapperImpl.CLEAR_SERVICE_WRAPPER;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class ClearServletPostRequestHandler implements Handler {

    public static final ClearServletPostRequestHandler CLEAR_SERVLET_POST_REQUEST_HANDLER = new ClearServletPostRequestHandler(CLEAR_SERVICE_WRAPPER);

    private final ClearServiceWrapper clearService;

    public ClearServletPostRequestHandler(final ClearServiceWrapper clearService) {
        guard(notNull(clearService));
        this.clearService = clearService;
    }

    // POST -- clear service
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.clearService.clear();
    }

}
