package unit.feed.controller;

import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.01.14
 */
public class ControllerUpdateFeedTitleTest extends AbstractControllerTest {

    private static final String UPDATED_TITLE = "updated_title";

    @Test
    public void whenFeedIdIsCorrectThenTitleUpdates() throws ServiceException {
        UUID feedId = addValidFirstRssFeed();

        this.controlService.updateFeedTitle(feedId, UPDATED_TITLE);

        final FeedHeader header = this.feedHeadersRepositoryStub.loadHeader(feedId);

        assertEquals(UPDATED_TITLE, header.title);
    }

    @Test(expected = ServiceException.class)
    public void whenFeedIdIsIncorrectThenExceptionThrows() throws ServiceException {
        this.controlService.updateFeedTitle(UUID.randomUUID(), UPDATED_TITLE);
    }

}
