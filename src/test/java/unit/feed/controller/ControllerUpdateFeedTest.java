package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.services.report.FeedUpdateReport;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.06.13
 */
public class ControllerUpdateFeedTest extends AbstractControllerTestBase {

    @Test(expected = ServiceException.class)
    public void whenFeedNotFoundThenExceptionThrows() throws ServiceException {
        this.updatesService.updateFeed(UUID.randomUUID());
    }

    @Test
    public void whenFeedUpdatedThenMergeReportReturns() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedUpdateReport report = this.updatesService.updateFeed(feedId);

        assertNotNull(report);

        assertEquals(feedId, report.feedId);
        assertEquals(VALID_FIRST_RSS_FEED_LINK, report.feedLink);
        assertEquals(0, report.mergeReport.removed.size());
        assertEquals(2, report.mergeReport.retained.size());
        assertEquals(0, report.mergeReport.added.size());
    }

    @Test
    public void whenFeedNotUpdatedThenStoreDoesNotCalled() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.updatesService.updateFeed(feedId);

        assertEquals(1, this.feedItemsRepositoryStub.getStoreCount());
    }

    @Test
    public void whenFeedIsUpdatedSuccessfullyThenRegistrationServiceIsNotified() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.updatesService.updateFeed(feedId);

        Mockito.verify(this.updateErrorRegistrationServiceSpy).updateSuccess(feedId);
        Mockito.verifyNoMoreInteractions(this.updateErrorRegistrationServiceSpy);
    }

    @Test
    public void whenFeedIsUpdatedNotSuccessfullyThenRegistrationServiceIsNotified() {
        final UUID randomFeedId = UUID.randomUUID();

        try {
            this.updatesService.updateFeed(randomFeedId);

            fail();
        } catch (ServiceException ignored) {
        }

        Mockito.verify(this.updateErrorRegistrationServiceSpy).updateError(randomFeedId);
        Mockito.verifyNoMoreInteractions(this.updateErrorRegistrationServiceSpy);
    }

}
