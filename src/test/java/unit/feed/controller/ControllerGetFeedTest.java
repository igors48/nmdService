package unit.feed.controller;

import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.Feed;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControllerGetFeedTest extends AbstractControllerTestBase {

    @Test
    public void whenFeedAddedThenItReturns() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();
        final Feed feed = this.feedsService.getFeed(feedId);

        assertNotNull(feed);
    }

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.feedsService.getFeed(UUID.randomUUID());
    }

}
