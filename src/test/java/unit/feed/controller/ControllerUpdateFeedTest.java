package unit.feed.controller;

import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.error.ServiceException;
import nmd.orb.services.FeedUpdateReport;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.06.13
 */
public class ControllerUpdateFeedTest extends AbstractControllerTestBase {

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.updatesService.updateFeed(UUID.randomUUID());
    }

    @Test
    public void whenFeedUpdatedThenMergeReportReturns() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedUpdateReport report = this.updatesService.updateFeed(feedId);

        assertNotNull(report);

        assertEquals(feedId, report.feedId);
        assertEquals(VALID_FIRST_RSS_FEED_LINK, report.feedLink);
        assertEquals(0, report.mergeReport.removed.size());
        assertEquals(2, report.mergeReport.retained.size());
        assertEquals(0, report.mergeReport.added.size());
    }

    @Test
    public void whenFeedUpdatedThenStatisticUpdated() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.updatesService.updateFeed(feedId);
        this.updatesService.updateFeed(feedId);

        final FeedUpdateTask feedUpdateTask = this.feedUpdateTaskRepositoryStub.loadTaskForFeedId(feedId);

        assertEquals(2, feedUpdateTask.executions);
        assertEquals(0, feedUpdateTask.updates);
    }

}
