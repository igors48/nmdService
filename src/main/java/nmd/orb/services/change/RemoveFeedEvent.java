package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RemoveFeedEvent implements Event {

    public final String feedTitle;

    public RemoveFeedEvent(final String feedTitle) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.feedTitle = feedTitle));
    }

}
