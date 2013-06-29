package feed.controller;

import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.06.13
 */
public class ControllerUpdateFeedTest extends ControllerTestBase {

    @Test(expected = ControlServiceException.class)
    public void whenNoScheduledTasksFoundThenExceptionThrows() throws ControlServiceException {
        this.controlService.updateCurrentFeed();
    }

    @Test(expected = ControlServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ControlServiceException {
        this.controlService.updateFeed(UUID.randomUUID());
    }

    @Test
    public void whenFeedUpdatedThenMergeReportReturns() throws Exception {
        final UUID feedId = addValidFirstRssFeed();

        final FeedItemsMergeReport report = this.controlService.updateFeed(feedId);

        Assert.assertNotNull(report);
    }

}
