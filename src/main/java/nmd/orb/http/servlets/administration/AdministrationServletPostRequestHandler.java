package nmd.orb.http.servlets.administration;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.AdministrationServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.invalidResetAction;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class AdministrationServletPostRequestHandler implements Handler {

    public static final String FULL_RESET = "full";
    public static final String CACHE_RESET = "cache";

    private final AdministrationServiceWrapper clearService;

    public AdministrationServletPostRequestHandler(final AdministrationServiceWrapper clearService) {
        guard(notNull(clearService));
        this.clearService = clearService;
    }

    // POST -- full reset service or reset cache only
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidResetAction(""));
        }

        final String action = elements.get(0);

        switch (action) {
            case FULL_RESET:
                return this.clearService.clear();
            case CACHE_RESET:
                return this.clearService.flushCache();
            default:
                return createErrorJsonResponse(invalidResetAction(action));
        }
    }

}
