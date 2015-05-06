package nmd.orb.collector.scheduler;

import nmd.orb.util.Parameter;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTask implements Serializable {

    public final UUID feedId;
    public final int maxFeedItemsCount;

    public FeedUpdateTask(final UUID feedId, final int maxFeedItemsCount) {
        guard(isValidFeedHeaderId(feedId));
        this.feedId = feedId;

        guard(isValidMaxFeedItemsCount(maxFeedItemsCount));
        this.maxFeedItemsCount = maxFeedItemsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedUpdateTask that = (FeedUpdateTask) o;

        if (maxFeedItemsCount != that.maxFeedItemsCount) return false;
        if (!feedId.equals(that.feedId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedId.hashCode();
        result = 31 * result + maxFeedItemsCount;
        return result;
    }

    public static boolean isValidMaxFeedItemsCount(final int count) {
        return Parameter.isPositive(count);
    }

}
