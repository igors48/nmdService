package unit.feed.controller;

import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.error.ServiceException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.06.13
 */
public class ControllerUpdateFeedTest extends AbstractControllerTestBase {

    @Test(expected = ServiceException.class)
    public void whenNoScheduledTasksFoundThenExceptionThrows() throws ServiceException {
        this.updatesService.updateCurrentFeed();
    }

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.updatesService.updateFeed(UUID.randomUUID());
    }

    @Test
    public void whenFeedUpdatedThenMergeReportReturns() throws Exception {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedUpdateReport report = this.updatesService.updateFeed(feedId);

        assertNotNull(report);
    }

}
