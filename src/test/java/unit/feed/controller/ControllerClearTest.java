package unit.feed.controller;

import nmd.orb.error.ServiceException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ControllerClearTest extends AbstractControllerTestBase {

    @Before
    @Override
    public void before() throws ServiceException {
        super.before();

        final UUID firstFeedId = addValidFirstRssFeedToMainCategory();
        final UUID secondFeedId = addValidSecondRssFeedToMainCategory();

        this.readsService.markItemAsRead(firstFeedId, "read_first");
        this.readsService.markItemAsRead(secondFeedId, "read_second");
        this.categoriesService.addCategory("category");

        this.feedsService.clear();
    }

    @Test
    public void whenClearedThenNoHeadersRemain() {
        assertTrue(this.feedHeadersRepositoryStub.isEmpty());
    }

    @Test
    public void whenClearedThenNoItemsRemain() {
        assertTrue(this.feedItemsRepositoryStub.isEmpty());
    }

    @Test
    public void whenClearedThenNoUpdateTasksRemain() {
        assertTrue(this.feedUpdateTaskRepositoryStub.isEmpty());
    }

    @Test
    public void whenClearedThenNoReadItemsRemain() {
        assertTrue(this.readFeedItemsRepositoryStub.isEmpty());
    }

    @Test
    public void whenClearedThenNoSchedulerContextRemain() {
        assertTrue(this.feedUpdateTaskSchedulerContextRepositoryStub.isEmpty());
    }

    @Test
    public void whenClearedThenNoCategoriesRemain() {
        assertTrue(this.categoriesRepositoryStub.isEmpty());
    }

}
