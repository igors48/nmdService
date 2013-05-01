package nmd.rss.collector.scheduler;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTask {

    public final UUID id;
    public final UUID feedId;

    public FeedUpdateTask(final UUID id, final UUID feedId) {
        assertNotNull(id);
        this.id = id;

        assertNotNull(feedId);
        this.feedId = feedId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FeedUpdateTask that = (FeedUpdateTask) o;

        if (!this.feedId.equals(that.feedId)) return false;

        if (!this.id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();

        result = 31 * result + this.feedId.hashCode();

        return result;
    }

}
