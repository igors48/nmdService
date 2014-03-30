package nmd.rss.collector.rest;

import java.util.UUID;

import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.feed.FeedItem.isValidFeedItemGuid;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * User: igu
 * Date: 28.11.13
 */
public class FeedAndItemIds {

    public final UUID feedId;
    public final String itemId;

    public FeedAndItemIds(final UUID feedId, final String itemId) {
        guard(isValidFeedHeaderId(feedId));
        this.feedId = feedId;

        guard(notNull(itemId) && (itemId.isEmpty() || isValidFeedItemGuid(itemId)));
        this.itemId = itemId;
    }

}
