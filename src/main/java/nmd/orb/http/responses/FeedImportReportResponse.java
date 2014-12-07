package nmd.orb.http.responses;

import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 07.12.2014.
 */
public class FeedImportReportResponse extends SuccessResponse {

    public static FeedImportReportResponse convert(final FeedImportStatusReport report) {
        guard(notNull(report));

        return new FeedImportReportResponse();
    }

}
