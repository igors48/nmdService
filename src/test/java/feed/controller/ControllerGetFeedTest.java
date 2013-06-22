package feed.controller;

import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.feed.Feed;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControllerGetFeedTest extends ControllerTestBase {

    @Test
    public void whenFeedAddedThenItReturns() throws ControlServiceException {
        final UUID feedId = addValidFirstRssFeed();
        final Feed feed = this.controlService.getFeed(feedId);

        assertNotNull(feed);
    }

    @Test(expected = ControlServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ControlServiceException {
        this.controlService.getFeed(UUID.randomUUID());
    }

}
