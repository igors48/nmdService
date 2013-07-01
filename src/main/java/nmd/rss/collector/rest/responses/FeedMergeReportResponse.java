package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedUpdateReport;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class FeedMergeReportResponse extends SuccessResponse {

    private String feedLink = null;
    private UUID feedId = null;
    private int removed = 0;
    private int retained = 0;
    private int added = 0;

    private FeedMergeReportResponse() {
        // empty
    }

    public static FeedMergeReportResponse convert(final FeedUpdateReport report) {
        assertNotNull(report);

        final FeedMergeReportResponse result = new FeedMergeReportResponse();

        result.feedId = report.feedId;
        result.feedLink = report.feedLink;
        result.added = report.mergeReport.added.size();
        result.removed = report.mergeReport.removed.size();
        result.retained = report.mergeReport.retained.size();

        return result;
    }

}
