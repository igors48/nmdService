package nmd.orb.http.responses;

import nmd.orb.http.responses.payload.FeedReadReportPayload;
import nmd.orb.services.FeedReadReport;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedReadReportsResponse extends SuccessResponse {

    public List<FeedReadReportPayload> reports;

    private FeedReadReportsResponse() {
        // empty
    }

    public static FeedReadReportsResponse convert(final List<FeedReadReport> reports) {
        guard(notNull(reports));

        final List<FeedReadReportPayload> helpers = new ArrayList<>();

        final FeedReadReportsResponse feedReadReportsResponse = new FeedReadReportsResponse();

        for (final FeedReadReport report : reports) {
            final FeedReadReportPayload helper = FeedReadReportPayload.create(report);

            helpers.add(helper);
        }

        feedReadReportsResponse.reports = helpers;

        return feedReadReportsResponse;
    }

}

