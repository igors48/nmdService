package nmd.orb.http.servlets.cron;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServletGetRequestHandler implements Handler {

    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return null;
    }

}
