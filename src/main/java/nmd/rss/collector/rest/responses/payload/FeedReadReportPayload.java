package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.FeedReadReport;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedReadReportPayload {

    public String feedId;
    public String feedTitle;
    public int read;
    public int notRead;
    public int readLater;
    public int addedFromLastVisit;
    public String topItemId;
    public String topItemLink;
    public long lastUpdate;

    private FeedReadReportPayload() {
        // empty
    }

    public static FeedReadReportPayload create(final FeedReadReport feedReadReport) {
        guard(notNull(feedReadReport));

        final FeedReadReportPayload feedReadReportHelper = new FeedReadReportPayload();

        feedReadReportHelper.feedId = feedReadReport.feedId.toString();
        feedReadReportHelper.feedTitle = feedReadReport.feedTitle;
        feedReadReportHelper.read = feedReadReport.read;
        feedReadReportHelper.notRead = feedReadReport.notRead;
        feedReadReportHelper.readLater = feedReadReport.readLater;
        feedReadReportHelper.addedFromLastVisit = feedReadReport.addedFromLastVisit;
        feedReadReportHelper.topItemId = feedReadReport.topItemId == null ? "" : feedReadReport.topItemId;
        feedReadReportHelper.topItemLink = feedReadReport.topItemLink == null ? "" : feedReadReport.topItemLink;
        feedReadReportHelper.lastUpdate = feedReadReport.lastUpdate.getTime();

        return feedReadReportHelper;
    }

}
