package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedItemReport;

import java.util.Calendar;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemReportHelper {

    private static final Calendar CALENDAR = Calendar.getInstance();

    private String feedId = "";
    private String title = "";
    private String link = "";
    private int day = 0;
    private int month = 0;
    private int hour = 0;
    private int minute = 0;
    private String guid = "";
    private boolean read = false;

    private FeedItemReportHelper() {
        // empty
    }

    public static FeedItemReportHelper convert(final FeedItemReport feedItemReport) {
        assertNotNull(feedItemReport);

        final FeedItemReportHelper helper = new FeedItemReportHelper();

        helper.feedId = feedItemReport.feedId.toString();
        helper.title = feedItemReport.title;
        helper.link = feedItemReport.link;
        helper.guid = feedItemReport.guid;
        helper.read = feedItemReport.read;

        CALENDAR.setTime(feedItemReport.date);
        helper.day = CALENDAR.get(Calendar.DAY_OF_MONTH);
        helper.month = CALENDAR.get(Calendar.MONTH);
        helper.hour = CALENDAR.get(Calendar.HOUR);
        helper.minute = CALENDAR.get(Calendar.MINUTE);

        return helper;
    }

}
