package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import org.junit.Test;
import org.mockito.Mockito;

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

    @Test
    public void whenFeedTitleUpdatesThenItIsRegistered() throws ServiceException {
        UUID feedId = addValidFirstRssFeedToMainCategory();
        String oldTitle = this.feedHeadersRepositoryStub.loadHeader(feedId).title;
        this.feedsService.updateFeedTitle(feedId, UPDATED_TITLE);

        Mockito.verify(this.changeRegistrationServiceSpy).registerRenameFeed(oldTitle, UPDATED_TITLE);
    }

    @Test(expected = ServiceException.class)
    public void whenFeedIdIsIncorrectThenExceptionThrows() throws ServiceException {
        this.feedsService.updateFeedTitle(UUID.randomUUID(), UPDATED_TITLE);
    }

}
