package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.services.report.FeedItemsReport;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public class ControllerMarkAllItemsAsReadTest extends AbstractControllerTestBase {

    @Test
    public void whenAllItemsMarkedAsReadAndTopItemTimeStampSetToZeroThenAllItemsMarked() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.readsService.markAllItemsAsRead(feedId, 0);

        final FeedItemsReport report = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);

        assertEquals(0, report.notRead);
        assertEquals(report.reports.size(), report.read);
    }

    @Test
    public void whenAllItemsMarkedAsReadAndTopItemTimeStampSetThenOnlyEarlierItemsMarked() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedItemsReport firstReport = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);
        final long olderItemTimestamp = firstReport.reports.get(1).date.getTime();

        this.readsService.markAllItemsAsRead(feedId, olderItemTimestamp);

        final FeedItemsReport secondReport = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);

        assertEquals(1, secondReport.notRead);
        assertEquals(1, secondReport.read);

        assertFalse(secondReport.reports.get(0).read);
        assertTrue(secondReport.reports.get(1).read);
    }

    @Test
    public void whenAllItemsMarkedAsReadThenReadLaterMarkDoesNotReset() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedItemsReport firstReport = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);
        final String firstItemId = firstReport.reports.get(0).itemId;

        this.readsService.toggleReadLaterItemMark(feedId, firstItemId);

        this.readsService.markAllItemsAsRead(feedId, 0);

        final FeedItemsReport secondReport = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);

        assertTrue(secondReport.reports.get(0).readLater);
    }

    @Test
    public void whenAllItemsMarkedAsReadAndTopItemTimeStampSetToZeroThenLastUpdateDateSetToYoungestFeedItemDate() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markAllItemsAsRead(feedHeader.id, 0);

        final Date lastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(lastUpdate, second.date);
    }

    @Test
    public void whenAllItemsMarkedAsReadAndTopItemTimeStampIsSetThenLastUpdateDateSetToThatTimestamp() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markAllItemsAsRead(feedHeader.id, first.date.getTime());

        final Date lastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(lastUpdate, first.date);
    }

}
