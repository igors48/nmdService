package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RenameFeedEvent implements Event {

    public final String oldFeedTitle;
    public final String newFeedTitle;

    public RenameFeedEvent(final String oldFeedTitle, final String newFeedTitle) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.oldFeedTitle = oldFeedTitle));
        guard(FeedHeader.isValidFeedHeaderTitle(this.newFeedTitle = newFeedTitle));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenameFeedEvent that = (RenameFeedEvent) o;
        return Objects.equals(oldFeedTitle, that.oldFeedTitle) &&
                Objects.equals(newFeedTitle, that.newFeedTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldFeedTitle, newFeedTitle);
    }

}
