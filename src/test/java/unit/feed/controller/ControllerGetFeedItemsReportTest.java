package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.services.reports.FeedItemsReport;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(2, feedItemsReport.reports.size());
        assertTrue(feedItemsReport.reports.get(0).date.getTime() > feedItemsReport.reports.get(1).date.getTime());
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedTitleSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.title, feedItemsReport.title);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedIdSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(feedHeader.id, feedItemsReport.id);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedLastUpdateTimeSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);
        final Date storedLastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(storedLastUpdate, feedItemsReport.lastUpdate);
    }

    @Test
    public void whenFeedItemsReportReturnsThenFeedItemCountersSetCorrectly() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);
        this.readsService.toggleReadLaterItemMark(feedHeader.id, second.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id);

        assertEquals(1, feedItemsReport.read);
        assertEquals(1, feedItemsReport.notRead);
        assertEquals(1, feedItemsReport.readLater);
    }

}
