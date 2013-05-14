package feed.gae;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.feed.FeedItemHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public class FeedEntityConversionTest {

    @Test
    public void roundtrip() {
        FeedItem origin = new FeedItem("title", "description", "link", 48);

        FeedItemHelper itemHelper = FeedItemHelper.convert(origin);
        FeedItem restored = FeedItemHelper.convert(itemHelper);

        assertEquals(origin, restored);
    }

}
