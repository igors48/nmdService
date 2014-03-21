package unit.feed.controller;

import nmd.rss.collector.controller.FeedSeriesUpdateReport;
import nmd.rss.collector.error.ServiceException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class ControllerUpdateCurrentFeedsTest extends AbstractControllerTestBase {

    @Test
    public void whenNoFeedsThenEmptyReportIsReturned() throws Exception {
        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(1));

        assertTrue(report.updated.isEmpty());
        assertTrue(report.errors.isEmpty());
    }

    @Test
    public void whenFeedIsUpdateCorrectlyThenItWillBeIncludedInUpdates() throws ServiceException {
        addValidFirstRssFeed();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(1));

        assertEquals(1, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

    @Test
    public void whenFeedIsUpdateWithErrorThenItWillBeIncludedInErrors() throws ServiceException {
        addValidFirstRssFeed();

        this.fetcherStub.simulateError(true);

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(1));

        assertEquals(1, report.errors.size());
        assertTrue(report.updated.isEmpty());
    }

    @Test
    public void whenCurrentFeedHadUpdatedAlreadyInThisCallThenUpdateProcessWillBeFinished() throws ServiceException {
        addValidFirstRssFeed();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(2));

        assertEquals(1, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

    @Test
    public void whenQuotaExpiredThenUpdateProcessWillBeFinished() throws ServiceException {
        addValidFirstRssFeed();
        addValidSecondRssFeed();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(3));

        assertEquals(2, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

}
