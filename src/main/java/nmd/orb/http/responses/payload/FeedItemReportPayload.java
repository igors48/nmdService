package nmd.orb.http.responses.payload;

import nmd.orb.services.report.FeedItemReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemReportPayload {

    public String feedId;
    public String title;
    public String link;
    public long date;
    public String itemId;
    public boolean read;
    public boolean readLater;
    public boolean addedSinceLastView;

    private FeedItemReportPayload() {
        // empty
    }

    public static FeedItemReportPayload create(final FeedItemReport feedItemReport) {
        guard(notNull(feedItemReport));

        final FeedItemReportPayload helper = new FeedItemReportPayload();

        helper.feedId = feedItemReport.feedId.toString();
        helper.title = feedItemReport.title;
        helper.link = feedItemReport.link;
        helper.itemId = feedItemReport.itemId;
        helper.read = feedItemReport.read;
        helper.readLater = feedItemReport.readLater;
        helper.addedSinceLastView = feedItemReport.addedSinceLastView;

        helper.date = feedItemReport.date.getTime();

        return helper;
    }

}
