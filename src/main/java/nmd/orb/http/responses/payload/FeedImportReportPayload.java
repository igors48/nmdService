package nmd.orb.http.responses.payload;

import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 07.12.2014.
 */
public class FeedImportReportPayload {

    public int scheduled;
    public int imported;
    public int failed;

    private FeedImportReportPayload() {
        // empty
    }

    public static FeedImportReportPayload convert(final FeedImportStatusReport report) {
        guard(notNull(report));

        final FeedImportReportPayload payload = new FeedImportReportPayload();

        payload.scheduled = report.getScheduled();
        payload.imported = report.getImported();
        payload.failed = report.getFailed();
        
        return payload;
    }

}
