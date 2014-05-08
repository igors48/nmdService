package nmd.rss.collector.scheduler;

import java.io.Serializable;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

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
        assertNotNull(feedId);
        this.feedId = feedId;

        assertPositive(maxFeedItemsCount);
        this.maxFeedItemsCount = maxFeedItemsCount;

        assertPositive(updates);
        this.updates = updates;

        assertPositive(executions);
        this.executions = executions;
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
