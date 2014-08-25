package unit.feed.controller;

import nmd.orb.collector.controller.FeedSeriesUpdateReport;
import nmd.orb.collector.error.ServiceException;
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
        addValidFirstRssFeedToMainCategory();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(1));

        assertEquals(1, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

    @Test
    public void whenFeedIsUpdateWithErrorThenItWillBeIncludedInErrors() throws ServiceException {
        addValidFirstRssFeedToMainCategory();

        this.fetcherStub.simulateError(true);

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(1));

        assertEquals(1, report.errors.size());
        assertTrue(report.updated.isEmpty());
    }

    @Test
    public void whenCurrentFeedHadUpdatedAlreadyInThisCallThenUpdateProcessWillBeFinished() throws ServiceException {
        addValidFirstRssFeedToMainCategory();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(2));

        assertEquals(1, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

    @Test
    public void whenQuotaExpiredThenUpdateProcessWillBeFinished() throws ServiceException {
        addValidFirstRssFeedToMainCategory();
        addValidSecondRssFeedToMainCategory();

        final FeedSeriesUpdateReport report = this.updatesService.updateCurrentFeeds(new CallsQuota(3));

        assertEquals(2, report.updated.size());
        assertTrue(report.errors.isEmpty());
    }

}
