package nmd.orb.http.wrappers;

import nmd.orb.gae.GaeServices;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.CronService;
import nmd.orb.services.quota.Quota;
import nmd.orb.services.quota.TimeQuota;
import nmd.orb.services.report.CronJobsReport;

import static nmd.orb.http.responses.CronJobsReportResponse.convert;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServiceWrapperImpl implements CronServiceWrapper {

    public static final CronServiceWrapperImpl CRON_SERVICE_WRAPPER = new CronServiceWrapperImpl(GaeServices.CRON_SERVICE);

    private static final long UPDATE_PERIOD = 9000;
    private static final long IMPORT_PERIOD = 9000;

    private final CronService cronService;

    public CronServiceWrapperImpl(final CronService cronService) {
        guard(notNull(cronService));
        this.cronService = cronService;
    }

    @Override
    public ResponseBody executeCronJobs() {
        final Quota updateQuota = new TimeQuota(UPDATE_PERIOD);
        final Quota importQuota = new TimeQuota(IMPORT_PERIOD);

        final CronJobsReport cronJobsReport = this.cronService.executeCronJobs(updateQuota, importQuota);

        return createJsonResponse(convert(cronJobsReport));
    }

}
