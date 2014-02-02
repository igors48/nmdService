package unit.feed.controller;

import nmd.rss.collector.controller.FeedItemsReport;
import nmd.rss.collector.error.ServiceException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public class ControllerMarkAllItemsAsReadTest extends AbstractControllerTest {

    @Test
    public void whenAllItemsMarkedAsReadThenAllItemsMarked() throws ServiceException {
        final UUID feedId = addValidFirstRssFeed();

        this.readsService.markAllItemsAsRead(feedId);

        final FeedItemsReport report = this.readsService.getFeedItemsReport(feedId);

        assertEquals(0, report.notRead);
        assertEquals(report.reports.size(), report.read);
    }

    @Test
    public void whenAllItemsMarkedAsReadThenReadLaterMarkDoesNotReset() throws ServiceException {
        final UUID feedId = addValidFirstRssFeed();

        final FeedItemsReport firstReport = this.readsService.getFeedItemsReport(feedId);
        final String firstItemId = firstReport.reports.get(0).itemId;

        this.readsService.toggleReadLaterItemMark(feedId, firstItemId);

        this.readsService.markAllItemsAsRead(feedId);

        final FeedItemsReport secondReport = this.readsService.getFeedItemsReport(feedId);

        assertTrue(secondReport.reports.get(0).readLater);
    }

}
