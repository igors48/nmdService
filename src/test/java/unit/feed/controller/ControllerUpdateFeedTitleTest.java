package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.01.14
 */
public class ControllerUpdateFeedTitleTest extends AbstractControllerTestBase {

    private static final String UPDATED_TITLE = "updated_title";

    @Test
    public void whenFeedIdIsCorrectThenTitleUpdates() throws ServiceException {
        UUID feedId = addValidFirstRssFeedToMainCategory();

        this.feedsService.updateFeedTitle(feedId, UPDATED_TITLE);

        final FeedHeader header = this.feedHeadersRepositoryStub.loadHeader(feedId);

        assertEquals(UPDATED_TITLE, header.title);
    }

    @Test(expected = ServiceException.class)
    public void whenFeedIdIsIncorrectThenExceptionThrows() throws ServiceException {
        this.feedsService.updateFeedTitle(UUID.randomUUID(), UPDATED_TITLE);
    }

}
