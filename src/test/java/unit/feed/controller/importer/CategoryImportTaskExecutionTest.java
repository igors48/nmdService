package unit.feed.controller.importer;

import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.ImportJob;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.CategoriesServiceAdapterStub;
import unit.feed.controller.stub.FeedsServiceAdapterStub;

import static org.junit.Assert.assertEquals;
import static unit.feed.controller.importer.CategoryImportContextTest.create;

/**
 * @author : igu
 */
public class CategoryImportTaskExecutionTest {

    private CategoriesServiceAdapterStub categoriesServiceAdapterStub;
    private FeedsServiceAdapterStub feedsServiceAdapterStub;

    @Before
    public void setUp() {
        this.categoriesServiceAdapterStub = new CategoriesServiceAdapterStub();
        this.feedsServiceAdapterStub = new FeedsServiceAdapterStub();
    }

    @Test
    public void whenCategoryCreationErrorThenContextStatusChangedToFailed() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.CATEGORY_CREATE);

        this.categoriesServiceAdapterStub.setThrowException(true);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(CategoryImportTaskStatus.FAILED, context.getStatus());
    }

    @Test
    public void whenStateIsCategoryCreateThenCategoriesAdapterCalledOnceWithCorrectParameter() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.CATEGORY_CREATE);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        this.categoriesServiceAdapterStub.assertCalledOnce(context.getCategoryName());
        this.feedsServiceAdapterStub.assertDidNotCalled();
    }

    @Test
    public void whenStateIsCategoryCreateAndCategoryCreatedThenCategoryIdIsStoredInContext() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.CATEGORY_CREATE);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(CategoriesServiceAdapterStub.CATEGORY_ID, context.getCategoryId());
    }

    @Test
    public void whenStateIsCategoryCreateAndCategoryCreatedThenContextStatusChangedToFeedsImport() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.CATEGORY_CREATE);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(CategoryImportTaskStatus.FEEDS_IMPORT, context.getStatus());
    }

    @Test
    public void whenThereIsNoExecutableWaitingTaskThenContextStatusChangedToFeedsErrorImport() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_IMPORT);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT, context.getStatus());
    }

    @Test
    public void whenThereIsNoExecutableErrorTaskThenContextStatusChangedToCompleted() {
        final CategoryImportContext context = create(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(CategoryImportTaskStatus.COMPLETED, context.getStatus());
    }
}
