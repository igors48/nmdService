package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RemoveFeedEvent implements Event {

    public final String feedTitle;

    public RemoveFeedEvent(final String feedTitle) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.feedTitle = feedTitle));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveFeedEvent that = (RemoveFeedEvent) o;
        return Objects.equals(feedTitle, that.feedTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedTitle);
    }

}
