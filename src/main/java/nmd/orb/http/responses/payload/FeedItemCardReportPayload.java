package nmd.orb.http.responses.payload;

import nmd.orb.services.FeedItemReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.HtmlTools.cleanupDescription;
import static nmd.orb.util.Parameter.notNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemCardReportPayload {

    public String feedId;
    public String title;
    public String link;
    public String description;
    public long date;
    public String itemId;
    public boolean read;
    public boolean readLater;
    public boolean addedSinceLastView;

    private FeedItemCardReportPayload() {
        // empty
    }

    public static FeedItemCardReportPayload create(final FeedItemReport feedItemReport) {
        guard(notNull(feedItemReport));

        final FeedItemCardReportPayload helper = new FeedItemCardReportPayload();

        helper.feedId = feedItemReport.feedId.toString();
        helper.title = feedItemReport.title;
        helper.link = feedItemReport.link;
        helper.description = cleanupDescription(feedItemReport.description);
        helper.itemId = feedItemReport.itemId;
        helper.read = feedItemReport.read;
        helper.readLater = feedItemReport.readLater;
        helper.addedSinceLastView = feedItemReport.addedSinceLastView;

        helper.date = feedItemReport.date.getTime();

        return helper;
    }

}
