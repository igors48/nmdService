package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsCardsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.08.2014
 */
public class ControllerGetFeedItemsCardsReportTest extends AbstractControllerTestBase {

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.readsService.getFeedItemsCardsReport(UUID.randomUUID(), 0, 5);
    }

    @Test
    public void whenFeedItemsCardsReportReturnsThenReportItemsSortFromNewToOld() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertEquals(2, feedItemsCardsReport.reports.size());
        assertTrue(feedItemsCardsReport.reports.get(0).date.getTime() > feedItemsCardsReport.reports.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsCardsReportReturnsThenFeedTitleSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertEquals(feedHeader.title, feedItemsCardsReport.title);
    }

    @Test
    public void whenFeedItemsCardsReportReturnsThenFeedIdSetCorrectly() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithTwoItems();

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertEquals(feedHeader.id, feedItemsCardsReport.feedId);
    }

}
