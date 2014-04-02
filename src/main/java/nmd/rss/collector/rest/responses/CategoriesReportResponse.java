package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.rest.responses.payload.CategoryReportPayload;

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

    public CategoriesReportResponse(final List<CategoryReport> reports) {
        super();

        guard(notNull(reports));
        this.reports = new ArrayList<>();

        for (final CategoryReport report : reports) {
            final CategoryReportPayload categoryReportPayload = CategoryReportPayload.create(report);
            this.reports.add(categoryReportPayload);
        }
    }

}
