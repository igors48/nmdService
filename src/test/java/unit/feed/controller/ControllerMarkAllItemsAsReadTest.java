package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public class ControllerMarkAllItemsAsReadTest extends AbstractControllerTestBase {

    @Test
    public void whenAllItemsMarkedAsReadThenAllItemsMarked() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.readsService.markAllItemsAsRead(feedId);

        final FeedItemsReport report = this.readsService.getFeedItemsReport(feedId);

        assertEquals(0, report.notRead);
        assertEquals(report.reports.size(), report.read);
    }

    @Test
    public void whenAllItemsMarkedAsReadThenReadLaterMarkDoesNotReset() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedItemsReport firstReport = this.readsService.getFeedItemsReport(feedId);
        final String firstItemId = firstReport.reports.get(0).itemId;

        this.readsService.toggleReadLaterItemMark(feedId, firstItemId);

        this.readsService.markAllItemsAsRead(feedId);

        final FeedItemsReport secondReport = this.readsService.getFeedItemsReport(feedId);

        assertTrue(secondReport.reports.get(0).readLater);
    }

    @Test
    public void whenAllItemsMarkedAsReadThenLastUpdateDateSetToYoungestFeedItemDate() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markAllItemsAsRead(feedHeader.id);

        final Date lastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(lastUpdate, second.date);
    }

}
