package unit.feed.controller;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 26.11.13
 */
public class ControllerMarkItemAsReadTest extends ControllerTestBase {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test
    public void whenItemMarkAsReadThenItDoesNotReturnAsNotRead() {
        final FeedHeader feedHeader = createSampleFeed();

        this.controlService.markItemAsRead(feedHeader.id, FEED_ITEM_GUID);
        final FeedItem notReadItem = this.controlService.getLatestNotReadItem(feedHeader.id);

        assertNull(notReadItem);
    }

    @Test
    public void whenItemMarkAsReadThenItStoresInRepository() {
        final FeedHeader feedHeader = createSampleFeed();

        this.controlService.markItemAsRead(feedHeader.id, FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id);

        assertTrue(readItems.contains(FEED_ITEM_GUID));
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() {
        final FeedHeader feedHeader = createSampleFeed();

        this.controlService.markItemAsRead(feedHeader.id, NOT_EXISTS_ID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id);

        assertTrue(readItems.isEmpty());
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() {
        final FeedHeader feedHeader = createSampleFeed();

        this.readFeedItemsRepositoryStub.store(feedHeader.id, new HashSet<String>() {{
            add(NOT_EXISTS_ID);
        }});

        this.controlService.markItemAsRead(feedHeader.id, FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id);

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

}
