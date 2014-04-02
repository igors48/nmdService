package nmd.rss.collector.rest.responses.helper;

import nmd.rss.collector.controller.FeedUpdateReport;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class FeedMergeReportResponseHelper {

    private String feedLink = null;
    private UUID feedId = null;
    private int removed = 0;
    private int retained = 0;
    private int added = 0;

    private FeedMergeReportResponseHelper() {
        // empty
    }

    public UUID getFeedId() {
        return this.feedId;
    }

    public FeedMergeReportResponseHelper(final String feedLink, final UUID feedId, final int removed, final int retained, final int added) {
        assertStringIsValid(feedLink);
        this.feedLink = feedLink;

        assertNotNull(feedId);
        this.feedId = feedId;

        assertPositive(removed);
        this.removed = removed;

        assertPositive(retained);
        this.retained = retained;

        assertPositive(added);
        this.added = added;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FeedMergeReportResponseHelper that = (FeedMergeReportResponseHelper) o;

        if (added != that.added) return false;
        if (removed != that.removed) return false;
        if (retained != that.retained) return false;
        if (feedId != null ? !feedId.equals(that.feedId) : that.feedId != null) return false;
        if (feedLink != null ? !feedLink.equals(that.feedLink) : that.feedLink != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (feedLink != null ? feedLink.hashCode() : 0);
        result = 31 * result + (feedId != null ? feedId.hashCode() : 0);
        result = 31 * result + removed;
        result = 31 * result + retained;
        result = 31 * result + added;
        return result;
    }

    public static FeedMergeReportResponseHelper convert(final FeedUpdateReport report) {
        assertNotNull(report);

        final FeedMergeReportResponseHelper result = new FeedMergeReportResponseHelper();

        result.feedId = report.feedId;
        result.feedLink = report.feedLink;
        result.added = report.mergeReport.added.size();
        result.removed = report.mergeReport.removed.size();
        result.retained = report.mergeReport.retained.size();

        return result;
    }

}
