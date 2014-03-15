package unit.feed.controller;

import nmd.rss.collector.error.ServiceException;
import org.junit.Test;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.05.13
 */
public class ControllerAddFeedTest extends AbstractControllerTestBase {

    @Test
    public void whenFeedFetchedOkAndParsedOkItAdds() throws ServiceException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(id);
    }

    @Test
    public void whenFeedWithSameLinkAddedSecondTimeThenPreviousIdReturns() throws ServiceException {
        final UUID firstId = addValidFirstRssFeed();
        final UUID secondId = addValidFirstRssFeed();

        assertEquals(firstId, secondId);
    }

    @Test
    public void whenFeedWithSameLinkButInDifferentCaseAddedSecondTimeThenPreviousIdReturns() throws ServiceException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        final UUID firstId = this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK.toUpperCase());
        final UUID secondId = this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK);

        assertEquals(firstId, secondId);
    }

    @Test
    public void whenFeedWithSameLinkButWithSlashAtTheEndAddedSecondTimeThenPreviousIdReturns() throws ServiceException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        final UUID firstId = this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK.toUpperCase());
        final UUID secondId = this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK + "/");

        assertEquals(firstId, secondId);
    }

    @Test(expected = ServiceException.class)
    public void whenFeedCanNotBeParsedThenExceptionOccurs() throws ServiceException {
        this.fetcherStub.setData(INVALID_RSS_FEED);

        this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK);
    }

    @Test
    public void whenFeedAddedThenNewUpdateTaskCreates() throws ServiceException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedUpdateTaskRepositoryStub.loadTaskForFeedId(id));
    }

    @Test
    public void whenFeedAddedThenItHeaderStores() throws ServiceException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedHeadersRepositoryStub.loadHeader(id));
    }

    @Test
    public void whenFeedAddedThenItItemsStores() throws ServiceException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.feedItemsRepositoryStub.loadItems(id));
    }

    @Test
    public void whenFeedAddedThenReadItemsCreatedForIt() throws ServiceException {
        final UUID id = addValidFirstRssFeed();

        assertNotNull(this.readFeedItemsRepositoryStub.load(id));
    }

}
