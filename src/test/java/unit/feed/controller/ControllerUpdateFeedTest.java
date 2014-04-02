package unit.feed.controller;

import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.error.ServiceException;
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
    public void whenFeedUpdatedThenMergeReportReturns() throws Exception {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedUpdateReport report = this.updatesService.updateFeed(feedId);

        assertNotNull(report);

        assertEquals(feedId, report.feedId);
        assertEquals(VALID_FIRST_RSS_FEED_LINK, report.feedLink);
        assertEquals(0, report.mergeReport.removed.size());
        assertEquals(2, report.mergeReport.retained.size());
        assertEquals(0, report.mergeReport.added.size());
    }

}
