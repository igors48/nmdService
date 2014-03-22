package nmd.rss.collector.rest.responses.helper;

import nmd.rss.collector.controller.FeedReadReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedReadReportHelper {

    private String feedId = "";
    private String feedTitle = "";
    private int read = 0;
    private int notRead = 0;
    private int readLater = 0;
    private int addedFromLastVisit = 0;
    private String topItemId = "";
    private String topItemLink = "";

    private FeedReadReportHelper() {
        // empty
    }

    public static FeedReadReportHelper convert(final FeedReadReport feedReadReport) {
        assertNotNull(feedReadReport);

        final FeedReadReportHelper feedReadReportHelper = new FeedReadReportHelper();

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
