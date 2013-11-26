package feed.controller;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

/**
 * User: igu
 * Date: 25.10.13
 */
public class ControllerGetFeedLatestNotReadItemTest extends ControllerTestBase {

    @Test
    public void whenNoReadItemsThenAllFeedItemsReturn() {
        final FeedHeader feedHeader = createSampleFeed();

        final FeedItem notReadItem = this.controlService.getLatestNotReadItem(feedHeader.id);
    }

}
