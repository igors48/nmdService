package unit.feed.controller;

import nmd.rss.collector.controller.FeedReadReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

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
        final List<FeedReadReport> report = this.controlService.getFeedsReadReport();

        assertTrue(report.isEmpty());
    }

    @Test
    public void whenFeedExistsThenItIsParticipateInReport() throws ServiceException {
        final FeedHeader feedHeader = createFeedWithOneItem();

        final List<FeedReadReport> report = this.controlService.getFeedsReadReport();

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

        final List<FeedReadReport> readReports = this.controlService.getFeedsReadReport();

        assertEquals(FIRST_FEED_ITEM_GUID, readReports.get(0).topItemId);
        assertEquals(FIRST_FEED_ITEM_LINK, readReports.get(0).topItemLink);
    }

}
