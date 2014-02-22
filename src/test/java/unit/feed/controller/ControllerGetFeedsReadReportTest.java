package unit.feed.controller;

import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: igu
 * Date: 22.10.13
 */
public class ControllerGetFeedsReadReportTest extends AbstractControllerTest {

    @Test
    public void whenNoFeedsThenEmptyReportReturns() throws ServiceException {
        final List<FeedReadReport> report = this.readsService.getFeedsReadReport();

        assertTrue(report.isEmpty());
    }

    @Test
    public void whenFeedExistsThenItIsParticipateInReport() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        final List<FeedReadReport> report = this.readsService.getFeedsReadReport();

        assertEquals(1, report.size());

        final FeedReadReport reportItem = report.get(0);

        assertEquals(feedHeader.id, reportItem.feedId);
        assertEquals(feedHeader.title, reportItem.feedTitle);
        assertEquals(1, reportItem.notRead);
        assertEquals(0, reportItem.read);
    }

    @Test
    public void whenNotReadItemExistsThenItReturns() {
        createFeedWithOneItem();

        final List<FeedReadReport> readReports = this.readsService.getFeedsReadReport();

        assertEquals(FIRST_FEED_ITEM_GUID, readReports.get(0).topItemId);
        assertEquals(FIRST_FEED_ITEM_LINK, readReports.get(0).topItemLink);
    }

    @Test
    public void whenFeedItemAddedAfterLastVisitThenItIncludedInAddedFromLastVisitCounter() throws ServiceException {
        final FeedItem first = new FeedItem(FIRST_FEED_ITEM_TITLE, FIRST_FEED_ITEM_DESCRIPTION, FIRST_FEED_ITEM_LINK, new Date(), true, FIRST_FEED_ITEM_GUID);

        final FeedHeader feedHeader = createSampleFeed(first);

        this.readsService.markItemAsRead(feedHeader.id, FIRST_FEED_ITEM_GUID);

        pauseOneMillisecond();

        final FeedItem second = new FeedItem(SECOND_FEED_ITEM_TITLE, SECOND_FEED_ITEM_DESCRIPTION, SECOND_FEED_ITEM_LINK, new Date(), true, SECOND_FEED_ITEM_GUID);

        final FeedItemsMergeReport feedItemsMergeReport = new FeedItemsMergeReport(
                new ArrayList<FeedItem>(),
                new ArrayList<FeedItem>() {{
                    add(first);
                }},
                new ArrayList<FeedItem>() {{
                    add(second);
                }}
        );
        this.feedItemsRepositoryStub.mergeItems(feedHeader.id, feedItemsMergeReport);

        final FeedReadReport firstReport = this.readsService.getFeedsReadReport().get(0);

        assertEquals(1, firstReport.addedFromLastVisit);
    }

}
