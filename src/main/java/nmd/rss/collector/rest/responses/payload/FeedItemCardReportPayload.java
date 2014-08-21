package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.FeedItemReport;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.HtmlTools.cleanupDescription;
import static nmd.rss.collector.util.Parameter.notNull;

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
