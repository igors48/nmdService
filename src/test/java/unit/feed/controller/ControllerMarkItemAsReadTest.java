package unit.feed.controller;

import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.reader.ReadFeedItems;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 26.11.13
 */
public class ControllerMarkItemAsReadTest extends AbstractControllerTest {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test
    public void whenItemMarkAsReadThenItDoesNotReturnAsNotRead() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);
        final List<FeedReadReport> readReports = this.controlService.getFeedsReadReport();

        assertNull(readReports.get(0).topItemId);
    }

    @Test
    public void whenItemMarkAsReadThenItStoresInRepository() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).itemIds;

        assertTrue(readItems.contains(FIRST_FEED_ITEM_GUID));
    }

    @Test
    public void whenItemsMarkAsReadThenLastUpdateDataUpdates() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.controlService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);
        final Date firstDate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        pauseOneMillisecond();

        this.controlService.markItemAsRead(feedHeader.id, SECOND_FEED_ITEM_GUID);
        final Date secondDate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(1, secondDate.compareTo(firstDate));
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.markItemAsRead(feedHeader.id, NOT_EXISTS_ID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).itemIds;

        assertTrue(readItems.isEmpty());
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readFeedItemsRepositoryStub.store(feedHeader.id, new ReadFeedItems(new Date(), new HashSet<String>() {{
            add(NOT_EXISTS_ID);
        }}));

        this.controlService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).itemIds;

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

    @Test(expected = ServiceException.class)
    public void whenTryToMarkItemOfNotExistsFeedThenErrorReturns() throws ServiceException {
        this.controlService.markItemAsRead(UUID.randomUUID(), FIRST_FEED_ITEM_GUID);
    }

    private static void pauseOneMillisecond() {

        try {
            Thread.sleep(1);
        } catch (InterruptedException ignore) {
            // empty
        }
    }

}
