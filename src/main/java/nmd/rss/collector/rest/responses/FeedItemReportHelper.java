package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedItemReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemReportHelper {

    private String feedId = "";
    private String title = "";
    private String link = "";
    private long date = 0;
    private String itemId = "";
    private boolean read = false;
    private boolean readLater = false;

    private FeedItemReportHelper() {
        // empty
    }

    public static FeedItemReportHelper convert(final FeedItemReport feedItemReport) {
        assertNotNull(feedItemReport);

        final FeedItemReportHelper helper = new FeedItemReportHelper();

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
