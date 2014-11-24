package nmd.orb.collector.scheduler;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTask implements Serializable {

    public final UUID feedId;
    public final int maxFeedItemsCount;

    public FeedUpdateTask(final UUID feedId, final int maxFeedItemsCount) {
        guard(notNull(feedId));
        this.feedId = feedId;

        guard(isPositive(maxFeedItemsCount));
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

}
