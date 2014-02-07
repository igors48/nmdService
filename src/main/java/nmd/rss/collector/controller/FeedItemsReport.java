package nmd.rss.collector.controller;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * User: igu
 * Date: 18.12.13
 */
public class FeedItemsReport {

    public final UUID id;
    public final String title;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final List<FeedItemReport> reports;

    public FeedItemsReport(final UUID id, final String title, final int read, final int notRead, final int readLater, final List<FeedItemReport> reports) {
        assertNotNull(id);
        this.id = id;

        assertStringIsValid(title);
        this.title = title;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertPositive(readLater);
        this.readLater = readLater;

        assertNotNull(reports);
        this.reports = reports;
    }

}
