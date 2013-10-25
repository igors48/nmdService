package feed.controller;

import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: igu
 * Date: 22.10.13
 */
public class ControllerGetFeedsReadReportTest extends ControllerTestBase {

    @Test
    public void whenNoFeedsThenEmptyReportReturns() throws ControlServiceException {
        final List<FeedReadReport> report = this.controlService.getFeedsReadReport();

        assertTrue(report.isEmpty());
    }

    @Test
    public void whenFeedExistsThenItIsParticipateInReport() throws ControlServiceException {
        final FeedHeader feedHeader = createSampleFeed();

        final List<FeedReadReport> report = this.controlService.getFeedsReadReport();

        assertEquals(1, report.size());

        final FeedReadReport reportItem = report.get(0);

        assertEquals(feedHeader.id, reportItem.feedId);
        assertEquals(feedHeader.feedLink, reportItem.feedLink);
        assertEquals(1, reportItem.notRead);
        assertEquals(0, reportItem.read);
    }

    //TODO it needs to create convenient method for that
    private FeedHeader createSampleFeed() {
        final FeedHeader feedHeader = new FeedHeader(UUID.randomUUID(), "feedLink", "title", "description", "link");
        this.feedHeadersRepositoryStub.storeHeader(feedHeader);

        final FeedItem feedItem = new FeedItem("title", "description", "link", new Date(), "guid");
        final List<FeedItem> feedItems = Arrays.asList(feedItem);
        final FeedItemsMergeReport feedItemsMergeReport = new FeedItemsMergeReport(new ArrayList<FeedItem>(), feedItems, new ArrayList<FeedItem>());
        this.feedItemsRepositoryStub.mergeItems(feedHeader.id, feedItemsMergeReport);

        return feedHeader;
    }
}
