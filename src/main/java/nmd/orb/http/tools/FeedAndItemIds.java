package nmd.orb.http.tools;

import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.feed.FeedItem.isValidFeedItemGuid;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
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
