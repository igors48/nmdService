package nmd.orb.http.responses.payload;

import nmd.orb.collector.controller.CategoryReport;
import nmd.orb.collector.controller.FeedReadReport;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoryExtendedReportPayload {

    public String id;
    public String name;
    public List<FeedReadReportPayload> feedReports;
    public int read;
    public int notRead;
    public int readLater;

    private CategoryExtendedReportPayload() {
        // empty
    }

    public static CategoryExtendedReportPayload create(final CategoryReport report) {
        guard(notNull(report));

        final CategoryExtendedReportPayload categoryReportPayload = new CategoryExtendedReportPayload();

        categoryReportPayload.id = report.id;
        categoryReportPayload.name = report.name;
        categoryReportPayload.read = report.read;
        categoryReportPayload.notRead = report.notRead;
        categoryReportPayload.readLater = report.readLater;

        categoryReportPayload.feedReports = new ArrayList<>();

        for (final FeedReadReport current : report.feedReadReports) {
            final FeedReadReportPayload feedReadReportPayload = FeedReadReportPayload.create(current);

            categoryReportPayload.feedReports.add(feedReadReportPayload);
        }

        return categoryReportPayload;
    }

}
