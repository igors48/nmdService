package feed.gae;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.gae.feed.header.FeedHeaderHelper;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class FeedHeaderConversionTest {

    @Test
    public void roundtrip() {
        final FeedHeader origin = new FeedHeader(UUID.randomUUID(), "feedLink", "title", "description", "link");

        final FeedHeaderHelper helper = FeedHeaderHelper.convert(origin);
        final FeedHeader restored = FeedHeaderHelper.convert(helper);

        assertEquals(origin, restored);
    }

}
