package nmd.rss.collector.rest;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 28.11.13
 */
public class FeedAndItemIds {

    public final UUID feedId;
    public final String itemId;

    public FeedAndItemIds(final UUID feedId, final String itemId) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertNotNull(itemId);
        this.itemId = itemId;
    }

}
