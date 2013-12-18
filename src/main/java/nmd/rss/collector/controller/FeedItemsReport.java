package nmd.rss.collector.controller;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 18.12.13
 */
public class FeedItemsReport {

    public final String title;
    public final int read;
    public final int notRead;
    public final List<FeedItemReport> reports;

    public FeedItemsReport(final String title, final int read, final int notRead, final List<FeedItemReport> reports) {
        assertStringIsValid(title);
        this.title = title;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertNotNull(reports);
        this.reports = reports;
    }

}
