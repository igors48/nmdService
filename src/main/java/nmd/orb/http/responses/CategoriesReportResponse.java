package nmd.orb.http.responses;

import nmd.orb.http.responses.payload.CategoryReportPayload;
import nmd.orb.http.responses.payload.VersionInfoPayload;
import nmd.orb.services.report.CategoryReport;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportResponse extends SuccessResponse {

    public VersionInfoPayload version;
    public List<CategoryReportPayload> reports;

    private CategoriesReportResponse() {
        // empty
    }

    public static CategoriesReportResponse create(final List<CategoryReport> reports) {
        guard(notNull(reports));

        final List<CategoryReportPayload> categoryReportPayloads = new ArrayList<>();

        for (final CategoryReport report : reports) {
            final CategoryReportPayload categoryReportPayload = CategoryReportPayload.create(report);
            categoryReportPayloads.add(categoryReportPayload);
        }

        final CategoriesReportResponse categoriesReportResponse = new CategoriesReportResponse();

        categoriesReportResponse.version = VersionInfoPayload.create();
        categoriesReportResponse.reports = categoryReportPayloads;

        return categoriesReportResponse;
    }

}
