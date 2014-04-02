package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.reader.Category;
import nmd.rss.reader.ReadFeedItems;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 26.11.13
 */
public class ControllerMarkItemAsReadTest extends AbstractControllerTestBase {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test
    public void whenItemMarkAsReadThenItDoesNotReturnAsNotRead() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);
        final List<FeedReadReport> readReports = this.readsService.getFeedsReadReport();

        assertNull(readReports.get(0).topItemId);
    }

    @Test
    public void whenItemMarkAsReadThenItStoresInRepository() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertTrue(readItems.contains(FIRST_FEED_ITEM_GUID));
    }

    @Test
    public void whenItemsMarkAsReadThenLastUpdateDataUpdates() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);
        final Date firstDate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        pauseOneMillisecond();

        this.readsService.markItemAsRead(feedHeader.id, SECOND_FEED_ITEM_GUID);
        final Date secondDate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(1, secondDate.compareTo(firstDate));
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readsService.markItemAsRead(feedHeader.id, NOT_EXISTS_ID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertTrue(readItems.isEmpty());
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readFeedItemsRepositoryStub.store(new ReadFeedItems(feedHeader.id, new Date(), new HashSet<String>() {{
            add(NOT_EXISTS_ID);
        }}, new HashSet<String>(), Category.MAIN_CATEGORY_ID));

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

    @Test
    public void whenItemMarkedReadThenReadLaterMarkResets() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readsService.toggleReadLaterItemMark(feedHeader.id, FIRST_FEED_ITEM_GUID);
        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final FeedItemsReport readReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(1, readReport.reports.size());
        assertTrue(readReport.reports.get(0).read);
        assertFalse(readReport.reports.get(0).readLater);
    }

    @Test(expected = ServiceException.class)
    public void whenTryToMarkItemOfNotExistsFeedThenErrorReturns() throws ServiceException {
        this.readsService.markItemAsRead(UUID.randomUUID(), FIRST_FEED_ITEM_GUID);
    }

}
