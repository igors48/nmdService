package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedItemReport;
import nmd.rss.collector.controller.FeedItemsReport;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemsReportResponse extends SuccessResponse {

    private String title;
    private int read;
    private int notRead;
    private List<FeedItemReportHelper> reports = null;

    private FeedItemsReportResponse() {
        // empty
    }

    public int getRead() {
        return this.read;
    }

    public int getNotRead() {
        return this.notRead;
    }

    public String getTitle() {
        return this.title;
    }

    public List<FeedItemReportHelper> getReports() {
        return this.reports;
    }

    public static FeedItemsReportResponse convert(final FeedItemsReport feedItemsReport) {
        assertNotNull(feedItemsReport);

        final List<FeedItemReportHelper> helpers = new ArrayList<>();

        for (final FeedItemReport feedItemReport : feedItemsReport.reports) {
            final FeedItemReportHelper helper = FeedItemReportHelper.convert(feedItemReport);

            helpers.add(helper);
        }

        final FeedItemsReportResponse response = new FeedItemsReportResponse();

        response.title = feedItemsReport.title;
        response.read = feedItemsReport.read;
        response.notRead = feedItemsReport.notRead;
        response.reports = helpers;

        return response;
    }

}
