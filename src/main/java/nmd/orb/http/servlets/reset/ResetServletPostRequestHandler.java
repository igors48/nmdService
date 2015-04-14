package nmd.orb.http.servlets.reset;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ResetServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public class ResetServletPostRequestHandler implements Handler {

    private final ResetServiceWrapper clearService;

    public ResetServletPostRequestHandler(final ResetServiceWrapper clearService) {
        guard(notNull(clearService));
        this.clearService = clearService;
    }

    // POST -- reset service
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.clearService.clear();
    }

}
