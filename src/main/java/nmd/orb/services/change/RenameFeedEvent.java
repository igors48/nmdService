package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenameFeedEvent that = (RenameFeedEvent) o;
        return Objects.equals(oldName, that.oldName) &&
                Objects.equals(newName, that.newName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldName, newName);
    }

}
