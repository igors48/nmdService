package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.rest.responses.payload.FeedMergeReportPayload;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class FeedMergeReportResponse extends SuccessResponse {

    public FeedMergeReportPayload report;

    private FeedMergeReportResponse() {
        // empty
    }

    public static FeedMergeReportResponse create(final UUID feedId, final String feedLink, final int removed, final int retained, final int added) {
        assertStringIsValid(feedLink);
        assertNotNull(feedId);
        assertPositive(removed);
        assertPositive(retained);
        assertPositive(added);

        final FeedMergeReportResponse response = new FeedMergeReportResponse();

        response.report = FeedMergeReportPayload.create(feedId, feedLink, removed, retained, added);

        return response;
    }

    public static FeedMergeReportResponse create(final FeedUpdateReport report) {
        assertNotNull(report);

        final FeedMergeReportResponse response = new FeedMergeReportResponse();

        response.report = FeedMergeReportPayload.create(report);

        return response;
    }
}
