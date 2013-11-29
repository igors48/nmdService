package unit.feed.controller;

import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.controller.FeedReadReport;
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

}
