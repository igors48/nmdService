package nmd.rss.collector.rest;

import nmd.rss.collector.controller.FeedUpdateReport;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
class FeedMergeReportResponse extends SuccessResponse {

    private String feedLink;
    private UUID feedId;
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

    private String getFeedLink() {
        return this.feedLink;
    }

    private void setFeedLink(final String feedLink) {
        this.feedLink = feedLink;
    }

    private UUID getFeedId() {
        return this.feedId;
    }

    private void setFeedId(final UUID feedId) {
        this.feedId = feedId;
    }

    public static FeedMergeReportResponse convert(final FeedUpdateReport report) {
        assertNotNull(report);

        final FeedMergeReportResponse result = new FeedMergeReportResponse();

        result.setFeedId(report.feedId);
        result.setFeedLink(report.feedLink);
        result.setAdded(report.mergeReport.added.size());
        result.setRemoved(report.mergeReport.removed.size());
        result.setRetained(report.mergeReport.retained.size());

        return result;
    }

}
