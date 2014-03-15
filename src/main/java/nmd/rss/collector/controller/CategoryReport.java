package nmd.rss.collector.controller;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class CategoryReport {

    public final String id;
    public final String name;
    public final List<UUID> feedIds;

    public CategoryReport(final String id, final String name, final List<UUID> feedIds) {
        assertStringIsValid(id);
        this.id = id;

        assertStringIsValid(name);
        this.name = name;

        assertNotNull(feedIds);
        this.feedIds = feedIds;
    }

}
