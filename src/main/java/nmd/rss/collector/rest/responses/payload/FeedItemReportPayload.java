package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.FeedItemReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemReportPayload {

    private String feedId = "";
    private String title = "";
    private String link = "";
    private long date = 0;
    private String itemId = "";
    private boolean read = false;
    private boolean readLater = false;

    private FeedItemReportPayload() {
        // empty
    }

    public static FeedItemReportPayload convert(final FeedItemReport feedItemReport) {
        assertNotNull(feedItemReport);

        final FeedItemReportPayload helper = new FeedItemReportPayload();

        helper.feedId = feedItemReport.feedId.toString();
        helper.title = feedItemReport.title;
        helper.link = feedItemReport.link;
        helper.itemId = feedItemReport.itemId;
        helper.read = feedItemReport.read;
        helper.readLater = feedItemReport.readLater;

        helper.date = feedItemReport.date.getTime();

        return helper;
    }

}
