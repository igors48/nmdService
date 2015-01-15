package unit.feed.controller.cron;

import nmd.orb.services.report.CronJobsReport;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;
import unit.feed.controller.CallsQuota;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class CronServiceTest extends AbstractControllerTestBase {

    @Test
    public void whenCronServiceCalledThenReportReturned() {
        final CallsQuota updatesQuota = new CallsQuota(1);
        final CallsQuota importsQuota = new CallsQuota(1);

        final CronJobsReport report = this.cronService.executeCronJobs(updatesQuota, importsQuota);

        assertEquals(0, report.feedSeriesUpdateReport.updated.size());
        assertEquals(0, report.feedSeriesUpdateReport.errors.size());
        assertEquals(0, report.feedImportStatusReport.getFailed());
        assertEquals(0, report.feedImportStatusReport.getImported());
        assertEquals(0, report.feedImportStatusReport.getScheduled());
    }

}
