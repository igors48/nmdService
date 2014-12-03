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

}
