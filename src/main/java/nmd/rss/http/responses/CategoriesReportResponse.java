package nmd.rss.http.responses;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.http.responses.payload.CategoryReportPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportResponse extends SuccessResponse {

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

        categoriesReportResponse.reports = categoryReportPayloads;

        return categoriesReportResponse;
    }

}
