package nmd.rss.http.responses;

import nmd.rss.collector.controller.FeedItemReport;
import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.http.responses.payload.FeedItemReportPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

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
    public List<FeedItemReportPayload> reports;
    public long lastUpdate;

    private FeedItemsReportResponse() {
        // empty
    }

    public static FeedItemsReportResponse convert(final FeedItemsReport feedItemsReport) {
        guard(notNull(feedItemsReport));

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
        response.lastUpdate = feedItemsReport.lastUpdate.getTime();

        return response;
    }

}
