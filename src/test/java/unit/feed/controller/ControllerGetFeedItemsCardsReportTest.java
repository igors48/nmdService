package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsCardsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

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

    @Test
    public void whenOffsetBiggerThanItemsCountThenEmptyReportReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 48, 5);

        assertTrue(feedItemsCardsReport.reports.isEmpty());
        assertTrue(feedItemsCardsReport.last);
        assertTrue(feedItemsCardsReport.first);
    }

    @Test
    public void whenOffsetIzZeroThenFirstFlagSet() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 5);

        assertTrue(feedItemsCardsReport.first);
    }

    @Test
    public void whenOffsetIsNotZeroThenFirstFlagReset() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 1, 5);

        assertFalse(feedItemsCardsReport.first);
    }

    @Test
    public void whenOffsetPlusSizeIsNotBiggerThanItemsCountThenFirstFlagReset() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 2);

        assertFalse(feedItemsCardsReport.last);
    }

    @Test
    public void whenOffsetPlusSizeIsEqualsToItemsCountThenFirstFlagSet() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 3);

        assertTrue(feedItemsCardsReport.last);
    }

    @Test
    public void whenOffsetPlusSizeBiggerThanItemsCountThenFirstFlagSet() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 4);

        assertTrue(feedItemsCardsReport.last);
    }

    @Test
    public void whenOffsetPlusSizeBiggerThanItemsCountThenLastPartOfListReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 2, 4);

        assertEquals(1, feedItemsCardsReport.reports.size());
        assertEquals(first.guid, feedItemsCardsReport.reports.get(0).itemId);
    }

    @Test
    public void whenOffsetPlusSizeBiggerThanItemsCountThenEntireListReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);
        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        final FeedItemsCardsReport feedItemsCardsReport = this.readsService.getFeedItemsCardsReport(feedHeader.id, 0, 48);

        assertEquals(3, feedItemsCardsReport.reports.size());
        assertEquals(third.guid, feedItemsCardsReport.reports.get(0).itemId);
        assertEquals(second.guid, feedItemsCardsReport.reports.get(1).itemId);
        assertEquals(first.guid, feedItemsCardsReport.reports.get(2).itemId);
    }

}
