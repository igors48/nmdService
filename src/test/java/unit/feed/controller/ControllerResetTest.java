package unit.feed.controller;

import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.services.UpdateErrorRegistrationService;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import nmd.orb.services.update.UpdateErrors;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.importer.ImportJobContextTest;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ControllerResetTest extends AbstractControllerTestBase {

    @Before
    @Override
    public void before() throws ServiceException {
        super.before();

        final UUID firstFeedId = addValidFirstRssFeedToMainCategory();
        final UUID secondFeedId = addValidSecondRssFeedToMainCategory();

        this.readsService.markItemAsRead(firstFeedId, "read_first");
        this.readsService.markItemAsRead(secondFeedId, "read_second");
        this.categoriesService.addCategory("category");

        final ImportJobContext context = ImportJobContextTest.create(ImportJobStatus.COMPLETED);
        this.importService.schedule(context);

        this.updateErrorsRepositoryStub.store(new UpdateErrors(firstFeedId, UpdateErrorRegistrationService.MAX_STORED_ERRORS_COUNT).appendError(ServiceError.feedParseError("http:\\domain.com")));
        this.updateErrorsRepositoryStub.store(new UpdateErrors(secondFeedId, UpdateErrorRegistrationService.MAX_STORED_ERRORS_COUNT).appendError(ServiceError.feedParseError("http:\\domain.com")));

        this.cacheStub.put("key", "value");

        this.administrationService.reset();
    }

    @Test
    public void whenResetThenNoHeadersRemain() {
        assertTrue(this.feedHeadersRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoItemsRemain() {
        assertTrue(this.feedItemsRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoUpdateTasksRemain() {
        assertTrue(this.feedUpdateTaskRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoReadItemsRemain() {
        assertTrue(this.readFeedItemsRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoSchedulerContextRemain() {
        assertTrue(this.feedUpdateTaskSchedulerContextRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoCategoriesRemain() {
        assertTrue(this.categoriesRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoImportJobContextRemain() {
        assertTrue(this.importJobContextRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoRegisteredChangeRemain() {
        assertTrue(this.changeRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenNoUpdateErrorsRemain() {
        assertTrue(this.updateErrorsRepositoryStub.isEmpty());
    }

    @Test
    public void whenResetThenCacheIsEmpty() {
        assertTrue(this.cacheStub.isEmpty());
    }

}
