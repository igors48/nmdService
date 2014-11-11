package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.services.report.FeedItemReport;
import nmd.orb.services.report.FeedItemsReport;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 13.12.13
 */
public class ControllerGetFeedItemsReportFilteredTest extends AbstractControllerTestBase {

    @Test
    public void whenShowNotReadFilterAppliedThenOnlyNotReadItemsReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id, FeedItemReportFilter.SHOW_NOT_READ);

        final List<FeedItemReport> items = feedItemsReport.reports;
        assertEquals(1, items.size());
        assertEquals(second.guid, items.get(0).itemId);

        assertEquals(1, feedItemsReport.notRead);
        assertEquals(1, feedItemsReport.read);
        assertEquals(0, feedItemsReport.readLater);
        assertEquals(1, feedItemsReport.addedSinceLastView);
    }

    @Test
    public void whenShowNotReadFilterAppliedAndThereAreNoNotReadItemsThenEmptyReportReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);
        this.readsService.markItemAsRead(feedHeader.id, second.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id, FeedItemReportFilter.SHOW_NOT_READ);

        final List<FeedItemReport> items = feedItemsReport.reports;
        assertTrue(items.isEmpty());

        assertEquals(0, feedItemsReport.notRead);
        assertEquals(2, feedItemsReport.read);
        assertEquals(0, feedItemsReport.readLater);
        assertEquals(0, feedItemsReport.addedSinceLastView);
    }

    @Test
    public void whenShowReadLaterFilterAppliedThenOnlyNotReadItemsReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.toggleReadLaterItemMark(feedHeader.id, first.guid);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id, FeedItemReportFilter.SHOW_READ_LATER);

        final List<FeedItemReport> items = feedItemsReport.reports;
        assertEquals(1, items.size());
        assertEquals(first.guid, items.get(0).itemId);

        assertEquals(2, feedItemsReport.notRead);
        assertEquals(0, feedItemsReport.read);
        assertEquals(1, feedItemsReport.readLater);
        assertEquals(2, feedItemsReport.addedSinceLastView);
    }

    @Test
    public void whenShowReadLaterFilterAppliedAndThereAreNoReadLaterItemsThenEmptyReportReturns() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedHeader feedHeader = createSampleFeed(first, second);

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedHeader.id, FeedItemReportFilter.SHOW_READ_LATER);

        assertTrue(feedItemsReport.reports.isEmpty());

        assertEquals(2, feedItemsReport.notRead);
        assertEquals(0, feedItemsReport.read);
        assertEquals(0, feedItemsReport.readLater);
        assertEquals(2, feedItemsReport.addedSinceLastView);
    }

}
