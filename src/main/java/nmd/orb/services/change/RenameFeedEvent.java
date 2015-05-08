package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RenameFeedEvent implements Event {

    public final String oldName;
    public final String newName;

    public RenameFeedEvent(final String oldName, final String newName) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.oldName = oldName));
        guard(FeedHeader.isValidFeedHeaderTitle(this.newName = newName));
    }

}
