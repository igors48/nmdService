package nmd.rss.collector.scheduler;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTask {

    public final UUID feedId;
    public final int maxFeedItemsCount;

    public FeedUpdateTask(final UUID feedId, final int maxFeedItemsCount) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertPositive(maxFeedItemsCount);
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
