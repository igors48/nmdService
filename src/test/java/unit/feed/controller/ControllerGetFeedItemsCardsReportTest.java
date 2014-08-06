package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsCardsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
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
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertTrue(feedItemsCardsReport.reports.get(0).date.getTime() > feedItemsCardsReport.reports.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsCardsReportReturnsThenFeedTitleSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertEquals(feedHeader.title, feedItemsCardsReport.title);
    }

    @Test
    public void whenFeedItemsCardsReportReturnsThenFeedIdSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertEquals(feedHeader.id, feedItemsCardsReport.feedId);
    }

}
