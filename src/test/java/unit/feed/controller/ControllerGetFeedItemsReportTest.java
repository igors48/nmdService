package unit.feed.controller;

import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.controller.FeedItemReport;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: igu
 * Date: 13.12.13
 */
public class ControllerGetFeedItemsReportTest extends AbstractControllerTest {

    @Test(expected = ControlServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ControlServiceException {
        this.controlService.getFeedItemsReport(UUID.randomUUID());
    }

    @Test
    public void whenFeedItemsReportReturnsThenReportItemsSortFromNewToOld() throws ControlServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final List<FeedItemReport> feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(2, feedItemsReport.size());
        assertTrue(feedItemsReport.get(0).date.getTime() > feedItemsReport.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedIdSetCorrectly() throws ControlServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final List<FeedItemReport> feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.id, feedItemsReport.get(0).feedId);
        assertEquals(feedHeader.id, feedItemsReport.get(1).feedId);
    }

    @Test
    public void whenFeedItemsReportReturnsThenReadItemsMarkedCorrectly() throws ControlServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        this.controlService.markItemAsRead(feedHeader.id, SECOND_FEED_ITEM_GUID);

        final List<FeedItemReport> feedItemsReport = this.controlService.getFeedItemsReport(feedHeader.id);

        assertTrue(feedItemsReport.get(0).read);
        assertFalse(feedItemsReport.get(1).read);
    }

}
