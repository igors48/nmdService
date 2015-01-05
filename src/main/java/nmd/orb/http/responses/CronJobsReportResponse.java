package nmd.orb.http.responses;

import nmd.orb.services.report.CronJobsReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CronJobsReportResponse extends SuccessResponse {

    public static CronJobsReportResponse convert(final CronJobsReport report) {
        guard(notNull(report));

        return null;
    }

}
