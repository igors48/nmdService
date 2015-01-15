package nmd.orb.http.responses;

import nmd.orb.http.responses.payload.FeedImportReportPayload;
import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 07.12.2014.
 */
public class FeedImportReportResponse extends SuccessResponse {

    public FeedImportReportPayload imports;

    public static FeedImportReportResponse convert(final FeedImportStatusReport report) {
        guard(notNull(report));

        final FeedImportReportResponse response = new FeedImportReportResponse();

        response.imports = FeedImportReportPayload.convert(report);

        return response;
    }

}
