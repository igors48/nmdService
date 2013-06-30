package nmd.rss.collector.rest;

import nmd.rss.collector.feed.FeedItemsMergeReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
class FeedMergeReportResponse extends SuccessResponse {

    private int removed = 0;
    private int retained = 0;
    private int added = 0;

    private FeedMergeReportResponse() {
        // empty
    }

    private int getRemoved() {
        return this.removed;
    }

    private void setRemoved(final int removed) {
        this.removed = removed;
    }

    private int getRetained() {
        return this.retained;
    }

    private void setRetained(final int retained) {
        this.retained = retained;
    }

    private int getAdded() {
        return this.added;
    }

    private void setAdded(final int added) {
        this.added = added;
    }

    public static FeedMergeReportResponse convert(final FeedItemsMergeReport report) {
        assertNotNull(report);

        final FeedMergeReportResponse result = new FeedMergeReportResponse();

        result.setAdded(report.added.size());
        result.setRemoved(report.removed.size());
        result.setRetained(report.retained.size());

        return result;
    }

}
