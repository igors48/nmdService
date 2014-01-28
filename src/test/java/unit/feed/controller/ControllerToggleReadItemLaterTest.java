package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.reader.ReadFeedItems;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 23.01.14
 */
public class ControllerToggleReadItemLaterTest extends AbstractControllerTest {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test
    public void whenItemMarkedAsReadLaterThenThisStateStores() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.toggleReadLaterItemMark(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.reports.get(0).readLater);
    }

    @Test
    public void whenItemMarkedAsReadLaterTwiceThenMarkResets() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.toggleReadLaterItemMark(feedHeader.id, FIRST_FEED_ITEM_GUID);
        this.controlService.toggleReadLaterItemMark(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertFalse(feedItemsReport.reports.get(0).readLater);
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.controlService.toggleReadLaterItemMark(feedHeader.id, "not-exists");

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(1, feedItemsReport.reports.size());
        assertEquals(FIRST_FEED_ITEM_GUID, feedItemsReport.reports.get(0).itemId);
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readFeedItemsRepositoryStub.store(feedHeader.id, new ReadFeedItems(new Date(),
                new HashSet<String>(),
                new HashSet<String>() {{
                    add(NOT_EXISTS_ID);
                    add(FIRST_FEED_ITEM_GUID);
                }}));

        this.controlService.toggleReadLaterItemMark(feedHeader.id, FIRST_FEED_ITEM_GUID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readLaterItemIds;

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

    @Test(expected = ServiceException.class)
    public void whenTryToToggleMarkItemOfNotExistsFeedThenErrorReturns() throws ServiceException {
        this.controlService.toggleReadLaterItemMark(UUID.randomUUID(), FIRST_FEED_ITEM_GUID);
    }

}
