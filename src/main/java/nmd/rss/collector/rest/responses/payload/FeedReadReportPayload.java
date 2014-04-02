package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.FeedReadReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedReadReportPayload {

    public String feedId = "";
    public String feedTitle = "";
    public int read = 0;
    public int notRead = 0;
    public int readLater = 0;
    public int addedFromLastVisit = 0;
    public String topItemId = "";
    public String topItemLink = "";

    private FeedReadReportPayload() {
        // empty
    }

    public static FeedReadReportPayload convert(final FeedReadReport feedReadReport) {
        assertNotNull(feedReadReport);

        final FeedReadReportPayload feedReadReportHelper = new FeedReadReportPayload();

        feedReadReportHelper.feedId = feedReadReport.feedId.toString();
        feedReadReportHelper.feedTitle = feedReadReport.feedTitle;
        feedReadReportHelper.read = feedReadReport.read;
        feedReadReportHelper.notRead = feedReadReport.notRead;
        feedReadReportHelper.readLater = feedReadReport.readLater;
        feedReadReportHelper.addedFromLastVisit = feedReadReport.addedFromLastVisit;
        feedReadReportHelper.topItemId = feedReadReport.topItemId == null ? "" : feedReadReport.topItemId;
        feedReadReportHelper.topItemLink = feedReadReport.topItemLink == null ? "" : feedReadReport.topItemLink;

        return feedReadReportHelper;
    }

}
