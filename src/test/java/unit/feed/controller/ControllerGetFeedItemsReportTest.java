package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 13.12.13
 */
public class ControllerGetFeedItemsReportTest extends AbstractControllerTestBase {

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.readsService.getFeedItemsReport(UUID.randomUUID());
    }

    @Test
    public void whenFeedItemsReportReturnsThenReportItemsSortFromNewToOld() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(2, feedItemsReport.reports.size());
        assertTrue(feedItemsReport.reports.get(0).date.getTime() > feedItemsReport.reports.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedIdSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.id, feedItemsReport.reports.get(0).feedId);
        assertEquals(feedHeader.id, feedItemsReport.reports.get(1).feedId);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedTitleSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.title, feedItemsReport.title);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedLastUpdateTimeSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);
        final Date storedLastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(storedLastUpdate, feedItemsReport.lastUpdate);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedItemCountersSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);
        this.readsService.toggleReadLaterItemMark(feedHeader.id, SECOND_FEED_ITEM_GUID);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(1, feedItemsReport.read);
        assertEquals(1, feedItemsReport.notRead);
        assertEquals(1, feedItemsReport.readLater);
    }

    @Test
    public void whenFeedItemsReportReturnsThenReadItemsMarkedCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.readsService.markItemAsRead(feedHeader.id, SECOND_FEED_ITEM_GUID);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.reports.get(0).read);
        assertFalse(feedItemsReport.reports.get(1).read);
    }

    @Test
    public void whenFeedItemAddedAfterLastUpdateThenItIsCorrectlyMarked() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        this.readsService.markAllItemsAsRead(feedHeader.id);

        final List<FeedItem> retainedItems = Arrays.asList(FIRST_FEED_ITEM);
        final List<FeedItem> addedItems = Arrays.asList(SECOND_FEED_ITEM);
        final FeedItemsMergeReport feedItemsMergeReport = new FeedItemsMergeReport(new ArrayList<FeedItem>(), retainedItems, addedItems);

        this.feedItemsRepositoryStub.storeItems(feedHeader.id, feedItemsMergeReport.getAddedAndRetained());

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.reports.get(0).addedSinceLastView);
        assertFalse(feedItemsReport.reports.get(1).addedSinceLastView);
    }

}
