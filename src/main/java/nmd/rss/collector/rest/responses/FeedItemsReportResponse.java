package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedItemReport;
import nmd.rss.collector.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemsReportResponse extends SuccessResponse {

    private List<FeedItemReportHelper> reports = null;

    private FeedItemsReportResponse() {
        // empty
    }

    public List<FeedItemReportHelper> getReports() {
        return this.reports;
    }

    public static FeedItemsReportResponse convert(final List<FeedItemReport> feedItemReports) {
        Assert.assertNotNull(feedItemReports);

        final List<FeedItemReportHelper> helpers = new ArrayList<>();

        for (final FeedItemReport feedItemReport : feedItemReports) {
            final FeedItemReportHelper helper = FeedItemReportHelper.convert(feedItemReport);

            helpers.add(helper);
        }

        final FeedItemsReportResponse response = new FeedItemsReportResponse();

        response.reports = helpers;

        return response;
    }

}
