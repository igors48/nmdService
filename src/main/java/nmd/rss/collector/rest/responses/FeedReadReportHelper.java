package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedReadReport;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedReadReportHelper {

    private String feedId = "";
    private String feedLink = "";
    private int read = 0;
    private int notRead = 0;

    private FeedReadReportHelper() {
        // empty
    }

    public static FeedReadReportHelper convert(final FeedReadReport feedReadReport) {
        assertNotNull(feedReadReport);

        final FeedReadReportHelper feedReadReportHelper = new FeedReadReportHelper();

        feedReadReportHelper.feedId = feedReadReport.feedId.toString();
        feedReadReportHelper.feedLink = feedReadReport.feedLink;
        feedReadReportHelper.read = feedReadReport.read;
        feedReadReportHelper.notRead = feedReadReport.notRead;

        return feedReadReportHelper;
    }

}