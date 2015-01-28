package nmd.orb.http.servlets.cron;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.CronServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServletGetRequestHandler implements Handler {

    private final CronServiceWrapper cronService;

    public CronServletGetRequestHandler(final CronServiceWrapper cronService) {
        guard(notNull(cronService));
        this.cronService = cronService;
    }

    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.cronService.executeCronJobs();
    }

}
