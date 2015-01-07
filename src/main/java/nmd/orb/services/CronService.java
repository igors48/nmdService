package nmd.orb.services;

import nmd.orb.services.quota.Quota;
import nmd.orb.services.report.CronJobsReport;
import nmd.orb.services.report.FeedImportStatusReport;
import nmd.orb.services.report.FeedSeriesUpdateReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CronService {

    private final UpdatesService updatesService;
    private final ImportService importService;

    public CronService(final UpdatesService updatesService, final ImportService importService) {
        guard(notNull(updatesService));
        this.updatesService = updatesService;

        guard(notNull(importService));
        this.importService = importService;
    }

    public CronJobsReport executeCronJobs(final Quota updateQuota, final Quota importQuota) {
        guard(notNull(updateQuota));
        guard(notNull(importQuota));

        updateQuota.start();
        final FeedSeriesUpdateReport feedSeriesUpdateReport = this.updatesService.updateCurrentFeeds(updateQuota);

        importQuota.start();
        final FeedImportStatusReport feedImportStatusReport = this.importService.executeSeries(importQuota);

        return new CronJobsReport(feedSeriesUpdateReport, feedImportStatusReport);
    }

}
