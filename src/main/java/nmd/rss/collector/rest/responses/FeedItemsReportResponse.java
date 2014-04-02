package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedItemReport;
import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.rest.responses.payload.FeedItemReportPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemsReportResponse extends SuccessResponse {

    public String id;
    public String title;
    public int read;
    public int notRead;
    public int readLater;
    public List<FeedItemReportPayload> reports = null;

    private FeedItemsReportResponse() {
        // empty
    }

    public static FeedItemsReportResponse convert(final FeedItemsReport feedItemsReport) {
        assertNotNull(feedItemsReport);

        final List<FeedItemReportPayload> helpers = new ArrayList<>();

        for (final FeedItemReport feedItemReport : feedItemsReport.reports) {
            final FeedItemReportPayload helper = FeedItemReportPayload.create(feedItemReport);

            helpers.add(helper);
        }

        final FeedItemsReportResponse response = new FeedItemsReportResponse();

        response.id = feedItemsReport.id.toString();
        response.title = feedItemsReport.title;
        response.read = feedItemsReport.read;
        response.notRead = feedItemsReport.notRead;
        response.readLater = feedItemsReport.readLater;
        response.reports = helpers;

        return response;
    }

}
