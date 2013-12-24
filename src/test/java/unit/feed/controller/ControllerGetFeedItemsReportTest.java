package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 13.12.13
 */
public class ControllerGetFeedItemsReportTest extends AbstractControllerTest {

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.controlService.getFeedItemsReport(UUID.randomUUID());
    }

    @Test
    public void whenFeedItemsReportReturnsThenReportItemsSortFromNewToOld() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(2, feedItemsReport.reports.size());
        assertTrue(feedItemsReport.reports.get(0).date.getTime() > feedItemsReport.reports.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedIdSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.id, feedItemsReport.reports.get(0).feedId);
        assertEquals(feedHeader.id, feedItemsReport.reports.get(1).feedId);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedTitleSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.title, feedItemsReport.title);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedItemCountersSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(0, feedItemsReport.read);
        assertEquals(2, feedItemsReport.notRead);
    }

    @Test
    public void whenFeedItemsReportReturnsThenReadItemsMarkedCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.controlService.markItemAsRead(feedHeader.id, SECOND_FEED_ITEM_GUID);

        final FeedItemsReport feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.reports.get(0).read);
        assertFalse(feedItemsReport.reports.get(1).read);
    }

}
