package unit.feed.controller.importer;

import nmd.orb.services.importer.*;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.CategoriesServiceAdapterStub;
import unit.feed.controller.stub.FeedsServiceAdapterStub;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class ImportJobExecutionTest {

    private CategoriesServiceAdapterStub categoriesServiceAdapterStub;
    private FeedsServiceAdapterStub feedsServiceAdapterStub;

    @Before
    public void setUp() {
        this.categoriesServiceAdapterStub = new CategoriesServiceAdapterStub();
        this.feedsServiceAdapterStub = new FeedsServiceAdapterStub();
    }

    @Test
    public void whenNoExecutableContextsThenImportJobContextMarkedAsCompleted() {
        final ImportJobContext context = ImportJobContextTest.create(ImportJobStatus.STARTED);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);

        assertEquals(ImportJobStatus.COMPLETED, context.getStatus());
    }

    @Test
    public void whenExecutableContextExistsThenItIsExecuted() {
        final CategoryImportContext categoryImportContext = CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE, FeedImportContextTest.create(3, FeedImportTaskStatus.WAITING));
        final ImportJobContext context = ImportJobContextTest.create(ImportJobStatus.STARTED, categoryImportContext);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);
        this.categoriesServiceAdapterStub.assertCalledOnce(CategoryImportContextTest.CATEGORY_NAME);

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);
        this.feedsServiceAdapterStub.assertCallOnce(FeedImportContextTest.HTTP_DOMAIN_COM, FeedImportContextTest.TITLE, categoryImportContext.getCategoryId());

        ImportJob.execute(context, this.categoriesServiceAdapterStub, this.feedsServiceAdapterStub);
        assertEquals(ImportJobStatus.COMPLETED, context.getStatus());
    }

}
