package feed.controller;

import nmd.rss.collector.controller.ControllerException;
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
    public void whenFeedAddedThenItReturns() throws ControllerException {
        final UUID feedId = addValidFirstRssFeed();
        final Feed feed = this.controlService.getFeed(feedId);

        assertNotNull(feed);
    }

    @Test(expected = ControllerException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ControllerException {
        this.controlService.getFeed(UUID.randomUUID());
    }

}
