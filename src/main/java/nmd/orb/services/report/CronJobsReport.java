package nmd.orb.services.report;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CronJobsReport {

    public final FeedSeriesUpdateReport feedSeriesUpdateReport;
    public final FeedImportStatusReport feedImportStatusReport;

    public CronJobsReport(final FeedSeriesUpdateReport feedSeriesUpdateReport, final FeedImportStatusReport feedImportStatusReport) {
        guard(notNull(feedSeriesUpdateReport));
        this.feedSeriesUpdateReport = feedSeriesUpdateReport;

        guard(notNull(feedImportStatusReport));
        this.feedImportStatusReport = feedImportStatusReport;
    }

}
