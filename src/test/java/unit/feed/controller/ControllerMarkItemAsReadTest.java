package unit.feed.controller;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * User: igu
 * Date: 26.11.13
 */
public class ControllerMarkItemAsReadTest extends ControllerTestBase {

    @Test
    public void whenItemMarkAsReadThenItDoesNotReturnAsNotRead() {
        final FeedHeader feedHeader = createSampleFeed();

        this.controlService.markItemAsRead(feedHeader.id, FEED_ITEM_GUID);
        final FeedItem notReadItem = this.controlService.getLatestNotReadItem(feedHeader.id);

        assertNull(notReadItem);
    }

}
