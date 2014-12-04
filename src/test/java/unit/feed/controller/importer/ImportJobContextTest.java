package unit.feed.controller.importer;

import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportJobContextTest {

    @Test
    public void whenThereIsAtLeastOneExecutableCategoryContextThenItIsReturned() {
        final CategoryImportContext categoryImportContext01 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);
        final CategoryImportContext categoryImportContext02 = CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE);

        final List<CategoryImportContext> contexts = new ArrayList<>();
        contexts.add(categoryImportContext01);
        contexts.add(categoryImportContext02);

        final ImportJobContext context = new ImportJobContext(contexts, ImportJobStatus.STARTED);
        final CategoryImportContext executableContext = context.findExecutableContext();

        assertEquals(categoryImportContext02, executableContext);
    }

    @Test
    public void whenThereIsNoExecutableCategoryContextThenNullIsReturned() {
        final CategoryImportContext categoryImportContext01 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);
        final CategoryImportContext categoryImportContext02 = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED);

        final List<CategoryImportContext> contexts = new ArrayList<>();
        contexts.add(categoryImportContext01);
        contexts.add(categoryImportContext02);

        final ImportJobContext context = new ImportJobContext(contexts, ImportJobStatus.STARTED);
        final CategoryImportContext executableContext = context.findExecutableContext();

        assertNull(executableContext);
    }

    @Test
    public void whenContextInStartedStateAndThereIsExecutableCategoryContextThenItCanBeExecuted() {
        fail();
    }

}
