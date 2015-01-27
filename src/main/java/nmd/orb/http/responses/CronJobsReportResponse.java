package nmd.orb.http.responses;

import nmd.orb.http.responses.payload.FeedImportReportPayload;
import nmd.orb.http.responses.payload.FeedSeriesUpdatePayload;
import nmd.orb.services.report.CronJobsReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CronJobsReportResponse extends SuccessResponse {

    public FeedImportReportPayload imports;
    public FeedSeriesUpdatePayload updates;
    public boolean autoExportMailWasSent;

    private CronJobsReportResponse() {
        // empty
    }

    public static CronJobsReportResponse convert(final CronJobsReport report) {
        guard(notNull(report));

        final CronJobsReportResponse response = new CronJobsReportResponse();

        response.imports = FeedImportReportPayload.convert(report.feedImportStatusReport);
        response.updates = FeedSeriesUpdatePayload.convert(report.feedSeriesUpdateReport);
        response.autoExportMailWasSent = report.autoExportMailWasSent;

        return response;
    }

}
