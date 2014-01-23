package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: igu
 * Date: 23.01.14
 */
public class ControllerToggleReadItemLaterTest extends AbstractControllerTest {

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

    //whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore
    //whenNotActualReadItemFoundWhileMarkingThenTheyRemoved
    //whenTryToMarkItemOfNotExistsFeedThenErrorReturns
}
