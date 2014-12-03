package unit.feed.controller.importer;

import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 02.12.2014.
 */
public class CategoryImportContextTest {

    public static final String CATEGORY_NAME = "categoryName";

    @Test
    public void completedContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.COMPLETED);

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void failedContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FAILED);

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenFeedsImportStateAndThereIsNoWaitingTasksThenContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_IMPORT, FeedImportContextTest.create(1, FeedImportTaskStatus.ERROR));

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenFeedsImportStateAndThereIsWaitingAndCanBeExecutedTasksExistsThenContextCanBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_IMPORT, FeedImportContextTest.create(1, FeedImportTaskStatus.WAITING));

        assertTrue(context.canBeExecuted());
    }

    @Test
    public void whenFeedsImportStateAndThereIsWaitingAndCanBeExecutedTasksNotExistsThenContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_IMPORT, FeedImportContextTest.create(0, FeedImportTaskStatus.WAITING));

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenFeedsWithErrorImportStateAndThereIsNoErrorTasksThenContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT, FeedImportContextTest.create(1, FeedImportTaskStatus.WAITING));

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenFeedsWithErrorImportStateAndThereIsErrorAndCanBeExecutedTasksExistsThenContextCanBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT, FeedImportContextTest.create(1, FeedImportTaskStatus.ERROR));

        assertTrue(context.canBeExecuted());
    }

    @Test
    public void whenFeedsWithErrorImportStateAndThereIsErrorAndCanBeExecutedTasksNotExistsThenContextCanNotBeExecuted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT, FeedImportContextTest.create(0, FeedImportTaskStatus.ERROR));

        assertFalse(context.canBeExecuted());
    }

    public static CategoryImportContext create(final CategoryImportTaskStatus status, FeedImportContext... contexts) {
        final List<FeedImportContext> feedImportContexts = Arrays.asList(contexts);

        return new CategoryImportContext(CATEGORY_NAME, feedImportContexts, status);
    }

}
