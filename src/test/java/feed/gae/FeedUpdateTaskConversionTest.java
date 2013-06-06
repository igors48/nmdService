package feed.gae;

import nmd.rss.collector.gae.task.FeedUpdateTaskEntity;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class FeedUpdateTaskConversionTest {

    @Test
    public void roundtrip() {
        final FeedUpdateTask origin = new FeedUpdateTask(UUID.randomUUID(), UUID.randomUUID(), 10);

        final FeedUpdateTaskEntity entity = FeedUpdateTaskEntity.convert(origin);
        final FeedUpdateTask restored = FeedUpdateTaskEntity.convert(entity);

        assertEquals(origin, restored);
    }

}
