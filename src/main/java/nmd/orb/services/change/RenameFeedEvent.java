package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RenameFeedEvent implements Event {

    private String oldFeedTitle;
    private String newFeedTitle;

    private RenameFeedEvent() {
    }

    public RenameFeedEvent(final String oldFeedTitle, final String newFeedTitle) {
        setOldFeedTitle(oldFeedTitle);
        setNewFeedTitle(newFeedTitle);
    }

    public String getOldFeedTitle() {
        return this.oldFeedTitle;
    }

    public void setOldFeedTitle(final String oldFeedTitle) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.oldFeedTitle = oldFeedTitle));
    }

    public String getNewFeedTitle() {
        return this.newFeedTitle;
    }

    public void setNewFeedTitle(final String newFeedTitle) {
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
