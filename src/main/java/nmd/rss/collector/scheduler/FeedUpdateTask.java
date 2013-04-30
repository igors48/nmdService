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

}
