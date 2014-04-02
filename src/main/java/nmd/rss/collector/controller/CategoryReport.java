package nmd.rss.collector.controller;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class CategoryReport {

    public final String id;
    public final String name;
    public final List<UUID> feedIds;
    public final int read;
    public final int notRead;
    public final int readLater;

    public CategoryReport(final String id, final String name, final List<UUID> feedIds, final int read, final int notRead, final int readLater) {
        assertStringIsValid(id);
        this.id = id;

        assertStringIsValid(name);
        this.name = name;

        assertNotNull(feedIds);
        this.feedIds = feedIds;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertPositive(readLater);
        this.readLater = readLater;
    }

}
