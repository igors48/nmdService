package unit.feed.controller;

import nmd.rss.collector.error.ServiceException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.06.13
 */
public class ControllerDeleteFeedTest extends AbstractControllerTest {

    @Test
    public void whenFeedRemovedThenItHeaderRemoved() throws ServiceException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedHeadersRepositoryStub.loadHeader(feedId));
    }

    @Test
    public void whenFeedRemovedThenUpdateTaskRemoved() throws ServiceException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedUpdateTaskRepositoryStub.loadTaskForFeedId(feedId));
    }

    @Test
    public void whenFeedRemovedThenItItemsRemoved() throws ServiceException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedItemsRepositoryStub.loadItems(feedId));
    }

    @Test
    public void whenFeedRemovedThenItReadItemsRemoved() throws ServiceException {
        final UUID feedId = createAndDeleteFeed();

        assertTrue(this.readFeedItemsRepositoryStub.load(feedId).readItemIds.isEmpty());
    }

    private UUID createAndDeleteFeed() throws ServiceException {
        final UUID feedId = addValidFirstRssFeed();
        this.controlService.removeFeed(feedId);

        return feedId;
    }

}
