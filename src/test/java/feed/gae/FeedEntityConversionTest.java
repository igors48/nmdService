package feed.gae;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.feed.FeedItemEntity;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public class FeedEntityConversionTest {

    @Test
    public void roundtrip() {
        FeedItem origin = new FeedItem(UUID.randomUUID(), "title", "description", "link", 48);

        FeedItemEntity feedEntity = FeedItemEntity.convert(UUID.randomUUID(), origin);
        FeedItem restored = FeedItemEntity.convert(feedEntity);

        assertEquals(origin, restored);
    }

}
