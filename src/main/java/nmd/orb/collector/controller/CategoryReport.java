package nmd.orb.collector.controller;

import java.util.List;

import static nmd.orb.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class CategoryReport {

    public final String id;
    public final String name;
    public final List<FeedReadReport> feedReadReports;
    public final int read;
    public final int notRead;
    public final int readLater;

    public CategoryReport(final String id, final String name, final List<FeedReadReport> feedReadReports, final int read, final int notRead, final int readLater) {
        assertStringIsValid(id);
        this.id = id;

        assertStringIsValid(name);
        this.name = name;

        assertNotNull(feedReadReports);
        this.feedReadReports = feedReadReports;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertPositive(readLater);
        this.readLater = readLater;
    }

}
