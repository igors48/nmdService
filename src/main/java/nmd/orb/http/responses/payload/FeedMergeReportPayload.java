package nmd.orb.http.responses.payload;

import nmd.orb.services.reports.FeedUpdateReport;

import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.04.2014
 */
public class FeedMergeReportPayload {

    public String feedLink;
    public UUID feedId;
    public int removed;
    public int retained;
    public int added;

    public static FeedMergeReportPayload create(final UUID feedId, final String feedLink, final int removed, final int retained, final int added) {
        guard(isValidFeedHeaderId(feedId));
        guard(isValidUrl(feedLink));
        guard(isPositive(removed));
        guard(isPositive(retained));
        guard(isPositive(added));

        final FeedMergeReportPayload feedMergeReportPayload = new FeedMergeReportPayload();

        feedMergeReportPayload.feedLink = feedLink;
        feedMergeReportPayload.feedId = feedId;
        feedMergeReportPayload.removed = removed;
        feedMergeReportPayload.retained = retained;
        feedMergeReportPayload.added = added;

        return feedMergeReportPayload;
    }

    public static FeedMergeReportPayload create(final FeedUpdateReport report) {
        guard(notNull(report));

        return create(report.feedId, report.feedLink, report.mergeReport.removed.size(), report.mergeReport.retained.size(), report.mergeReport.added.size());
    }

}
