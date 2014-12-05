package unit.feed.controller.importer;

import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportJobContextTest {

    @Test
    public void whenThereIsAtLeastOneExecutableCategoryContextThenItIsReturned() {
        final CategoryImportContext categoryImportContext01 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);
        final CategoryImportContext categoryImportContext02 = CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE);

        final ImportJobContext context = create(ImportJobStatus.STARTED, categoryImportContext01, categoryImportContext02);
        final CategoryImportContext executableContext = context.findExecutableContext();

        assertEquals(categoryImportContext02, executableContext);
    }

    @Test
    public void whenThereIsNoExecutableCategoryContextThenNullIsReturned() {
        final CategoryImportContext categoryImportContext01 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);
        final CategoryImportContext categoryImportContext02 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);

        final ImportJobContext context = create(ImportJobStatus.STARTED, categoryImportContext01, categoryImportContext02);
        final CategoryImportContext executableContext = context.findExecutableContext();

        assertNull(executableContext);
    }

    @Test
    public void whenContextInStoppedThenItCanNotBeExecuted() {
        final ImportJobContext context = create(ImportJobStatus.STOPPED);

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenContextInCompletedThenItCanNotBeExecuted() {
        final ImportJobContext context = create(ImportJobStatus.COMPLETED);

        assertFalse(context.canBeExecuted());
    }

    @Test
    public void whenContextInStarted() {
        fail();
    }

    private static ImportJobContext create(final ImportJobStatus status, CategoryImportContext... contexts) {
        return new ImportJobContext(asList(contexts), status);
    }

}
