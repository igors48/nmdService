package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.rest.responses.payload.CategoryExtendedReportPayload;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoryReportResponse extends SuccessResponse {

    public CategoryExtendedReportPayload report;

    private CategoryReportResponse() {
        // empty
    }

    public static CategoryReportResponse create(final CategoryReport report) {
        guard(notNull(report));

        final CategoryReportResponse categoryReportResponse = new CategoryReportResponse();

        categoryReportResponse.report = CategoryExtendedReportPayload.create(report);

        return categoryReportResponse;
    }

}
