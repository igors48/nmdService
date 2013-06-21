package feed.controller;

import nmd.rss.collector.controller.ControllerException;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.06.13
 */
public class ControllerDeleteFeedTest extends ControllerTestBase {

    @Test
    public void whenFeedRemovedThenItHeaderRemoved() throws ControllerException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedHeadersRepositoryStub.loadHeader(feedId));
    }

    @Test
    public void whenFeedRemovedThenUpdateTaskRemoved() throws ControllerException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedUpdateTaskRepositoryStub.loadTaskForFeedId(feedId));
    }

    @Test
    public void whenFeedRemovedThenItItemsRemoved() throws ControllerException {
        final UUID feedId = createAndDeleteFeed();

        assertNull(this.feedItemsRepositoryStub.loadItems(feedId));
    }

    private UUID createAndDeleteFeed() throws ControllerException {
        final UUID feedId = addValidFirstRssFeed();
        this.controlService.removeFeed(feedId);

        return feedId;
    }

}
