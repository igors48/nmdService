package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.reader.Category;
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
public class ControllerToggleReadItemLaterTest extends AbstractControllerTestBase {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test
    public void whenItemMarkedAsReadLaterThenThisStateStores() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readsService.toggleReadLaterItemMark(feedHeader.id, feedItem.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.reports.get(0).readLater);
    }

    @Test
    public void whenItemMarkedAsReadLaterTwiceThenMarkResets() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readsService.toggleReadLaterItemMark(feedHeader.id, feedItem.guid);
        this.readsService.toggleReadLaterItemMark(feedHeader.id, feedItem.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertFalse(feedItemsReport.reports.get(0).readLater);
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readsService.toggleReadLaterItemMark(feedHeader.id, "not-exists");

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(1, feedItemsReport.reports.size());
        assertEquals(feedItem.guid, feedItemsReport.reports.get(0).itemId);
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readFeedItemsRepositoryStub.store(new ReadFeedItems(feedHeader.id, new Date(),
                new HashSet<String>(),
                new HashSet<String>() {{
                    add(NOT_EXISTS_ID);
                    add(feedItem.guid);
                }}, Category.MAIN_CATEGORY_ID));

        this.readsService.toggleReadLaterItemMark(feedHeader.id, feedItem.guid);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readLaterItemIds;

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

    @Test(expected = ServiceException.class)
    public void whenTryToToggleMarkItemOfNotExistsFeedThenErrorReturns() throws ServiceException {
        this.readsService.toggleReadLaterItemMark(UUID.randomUUID(), UUID.randomUUID().toString());
    }

}
