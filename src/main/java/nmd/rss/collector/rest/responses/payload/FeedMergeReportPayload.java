package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.FeedUpdateReport;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.04.2014
 */
public class FeedMergeReportPayload {

    public String feedLink = null;
    public UUID feedId = null;
    public int removed = 0;
    public int retained = 0;
    public int added = 0;

    public static FeedMergeReportPayload create(final UUID feedId, final String feedLink, final int removed, final int retained, final int added) {
        assertStringIsValid(feedLink);
        assertNotNull(feedId);
        assertPositive(removed);
        assertPositive(retained);
        assertPositive(added);

        final FeedMergeReportPayload feedMergeReportPayload = new FeedMergeReportPayload();

        feedMergeReportPayload.feedLink = feedLink;
        feedMergeReportPayload.feedId = feedId;
        feedMergeReportPayload.removed = removed;
        feedMergeReportPayload.retained = retained;
        feedMergeReportPayload.added = added;

        return feedMergeReportPayload;
    }

    public static FeedMergeReportPayload create(final FeedUpdateReport report) {
        assertNotNull(report);

        return create(report.feedId, report.feedLink, report.mergeReport.removed.size(), report.mergeReport.retained.size(), report.mergeReport.added.size());
    }

}
