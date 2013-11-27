package feed.controller;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 25.10.13
 */
public class ControllerGetFeedLatestNotReadItemTest extends ControllerTestBase {

    @Test
    public void whenNotReadItemExistsThenItReturns() {
        final FeedHeader feedHeader = createSampleFeed();

        final FeedItem notReadItem = this.controlService.getLatestNotReadItem(feedHeader.id);

        assertEquals(FEED_ITEM_GUID, notReadItem.guid);
    }

}
