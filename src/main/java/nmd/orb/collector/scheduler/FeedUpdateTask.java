package nmd.orb.collector.scheduler;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.isPositive;
import static nmd.orb.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTask implements Serializable {

    public final UUID feedId;
    public final int maxFeedItemsCount;
    public final int updates;
    public final int executions;

    public FeedUpdateTask(final UUID feedId, final int maxFeedItemsCount) {
        this(feedId, maxFeedItemsCount, 0, 0);
    }

    public FeedUpdateTask(final UUID feedId, final int maxFeedItemsCount, final int updates, final int executions) {
        guard(notNull(feedId));
        this.feedId = feedId;

        guard(isPositive(maxFeedItemsCount));
        this.maxFeedItemsCount = maxFeedItemsCount;

        guard(isPositive(updates));
        this.updates = updates;

        guard(isPositive(executions));
        this.executions = executions;
    }

    public FeedUpdateTask updateStatistic(final int updated) {
        guard(isPositive(updated));

        final int newUpdates = this.updates + updated;
        final int newExecutions = this.executions + 1;

        return new FeedUpdateTask(this.feedId, this.maxFeedItemsCount, newUpdates, newExecutions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedUpdateTask that = (FeedUpdateTask) o;

        if (executions != that.executions) return false;
        if (maxFeedItemsCount != that.maxFeedItemsCount) return false;
        if (updates != that.updates) return false;
        if (!feedId.equals(that.feedId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedId.hashCode();
        result = 31 * result + maxFeedItemsCount;
        result = 31 * result + updates;
        result = 31 * result + executions;
        return result;
    }

}
