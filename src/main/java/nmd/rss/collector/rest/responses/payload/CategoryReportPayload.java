package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.controller.CategoryReport;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

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

    public CategoryReportPayload(final CategoryReport report) {
        guard(notNull(report));

        this.id = report.id;
        this.name = report.name;
        this.feedCount = report.feedIds.size();
        this.read = report.read;
        this.notRead = report.notRead;
        this.readLater = report.readLater;
    }

}
