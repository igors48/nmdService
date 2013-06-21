package feed.controller;

import nmd.rss.collector.controller.ControllerException;
import org.junit.Test;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.05.13
 */
public class ControllerAddFeedTest extends ControllerTestBase {

    @Test
    public void whenFeedFetchedOkAndParsedOkItAdds() throws ControllerException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(id);
    }

    @Test
    public void whenFeedWithSameLinkAddedSecondTimeThenPreviousIdReturns() throws ControllerException {
        final UUID firstId = addValidFirstRssFeed();
        final UUID secondId = addValidFirstRssFeed();

        assertEquals(firstId, secondId);
    }

    @Test
    public void whenFeedWithSameLinkButInDifferentCaseAddedSecondTimeThenPreviousIdReturns() throws ControllerException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        final UUID firstId = controlService.addFeed(VALID_FIRST_RSS_FEED_LINK.toUpperCase());
        final UUID secondId = controlService.addFeed(VALID_FIRST_RSS_FEED_LINK);

        assertEquals(firstId, secondId);
    }

    @Test(expected = ControllerException.class)
    public void whenFeedCanNotBeParsedThenExceptionOccurs() throws ControllerException {
        this.fetcherStub.setData(INVALID_RSS_FEED);

        controlService.addFeed(VALID_FIRST_RSS_FEED_LINK);
    }

    @Test
    public void whenFeedAddedThenNewUpdateTaskCreates() throws ControllerException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedUpdateTaskRepositoryStub.loadTaskForFeedId(id));
    }

    @Test
    public void whenFeedAddedThenItHeaderStores() throws ControllerException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedHeadersRepositoryStub.loadHeader(id));
    }

    @Test
    public void whenFeedAddedThenItItemsStores() throws ControllerException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedItemsRepositoryStub.loadItems(id));
    }

}
