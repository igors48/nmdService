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
public class ControllerUpdateFeedTest extends AbstractControllerTest {

    @Test(expected = ServiceException.class)
    public void whenNoScheduledTasksFoundThenExceptionThrows() throws ServiceException {
        this.controlService.updateCurrentFeed();
    }

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.controlService.updateFeed(UUID.randomUUID());
    }

    @Test
    public void whenFeedUpdatedThenMergeReportReturns() throws Exception {
        final UUID feedId = addValidFirstRssFeed();

        final FeedUpdateReport report = this.controlService.updateFeed(feedId);

        assertNotNull(report);
    }

}
