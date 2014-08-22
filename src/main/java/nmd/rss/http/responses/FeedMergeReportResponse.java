package nmd.rss.http.responses;

import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.http.responses.payload.FeedMergeReportPayload;

import java.util.UUID;

import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.*;

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
        guard(isValidFeedHeaderId(feedId));
        guard(isValidUrl(feedLink));
        guard(isPositive(removed));
        guard(isPositive(retained));
        guard(isPositive(added));

        final FeedMergeReportResponse response = new FeedMergeReportResponse();

        response.report = FeedMergeReportPayload.create(feedId, feedLink, removed, retained, added);

        return response;
    }

    public static FeedMergeReportResponse create(final FeedUpdateReport report) {
        guard(notNull(report));

        final FeedMergeReportResponse response = new FeedMergeReportResponse();

        response.report = FeedMergeReportPayload.create(report);

        return response;
    }
}
