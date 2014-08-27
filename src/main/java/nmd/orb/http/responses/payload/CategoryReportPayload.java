package nmd.orb.http.responses.payload;

import nmd.orb.services.CategoryReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoryReportPayload {

    public String id;
    public String name;
    public int feedCount;
    public int read;
    public int notRead;
    public int readLater;

    private CategoryReportPayload() {
        // empty
    }

    public static CategoryReportPayload create(final CategoryReport report) {
        guard(notNull(report));

        final CategoryReportPayload categoryReportPayload = new CategoryReportPayload();

        categoryReportPayload.id = report.id;
        categoryReportPayload.name = report.name;
        categoryReportPayload.feedCount = report.feedReadReports.size();
        categoryReportPayload.read = report.read;
        categoryReportPayload.notRead = report.notRead;
        categoryReportPayload.readLater = report.readLater;

        return categoryReportPayload;
    }

}
