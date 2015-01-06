package unit.feed.controller.importer;

import nmd.orb.services.importer.*;
import nmd.orb.services.report.FeedImportStatusReport;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportJobContextTest {

    @Test
    public void statusReportCalculatedCorrectly() {
        final CategoryImportContext categoryImportContext01 = CategoryImportContextTest.create(
                CategoryImportTaskStatus.COMPLETED,
                FeedImportContextTest.create(3, FeedImportTaskStatus.COMPLETED));
        final CategoryImportContext categoryImportContext02 = CategoryImportContextTest.create(
                CategoryImportTaskStatus.CATEGORY_CREATE,
                FeedImportContextTest.create(3, FeedImportTaskStatus.WAITING),
                FeedImportContextTest.create(3, FeedImportTaskStatus.FAILED));

        final ImportJobContext context = create(ImportJobStatus.STARTED, categoryImportContext01, categoryImportContext02);
        final FeedImportStatusReport actual = context.getStatusReport();

        final FeedImportStatusReport expected = new FeedImportStatusReport(ImportJobStatus.STARTED, 3, 1, 1);

        assertEquals(actual, expected);
    }

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

    public static ImportJobContext create(final ImportJobStatus status, final CategoryImportContext... contexts) {
        return new ImportJobContext(asList(contexts), status);
    }

}
