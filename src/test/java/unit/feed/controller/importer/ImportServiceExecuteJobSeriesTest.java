package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.*;
import org.junit.Test;
import unit.feed.controller.CallsQuota;

/**
 * Created by igor on 17.12.2014.
 */
public class ImportServiceExecuteJobSeriesTest extends AbstractImportServiceTest {

    @Test
    public void whenQuotaExpiredThenSeriesExecutionEnds() throws ServiceException {
        final CallsQuota quota = new CallsQuota(1);
        final ImportJobContext original = ImportJobContextTest.create(ImportJobStatus.STOPPED, CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE));

        this.importService.schedule(original);
        this.importService.start();

        this.importService.executeSeries(quota);

        this.categoriesServiceAdapterStub.assertCalledOnce(CategoryImportContextTest.CATEGORY_NAME);
        this.feedsServiceAdapterStub.assertDidNotCalled();
    }

    @Test
    public void whenThereAreNoJobsThenSeriesExecutionEnds() throws ServiceException {
        final CallsQuota quota = new CallsQuota(1000000);
        final CategoryImportContext categoryImportContext = CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE, FeedImportContextTest.create(1, FeedImportTaskStatus.WAITING));
        final ImportJobContext original = ImportJobContextTest.create(ImportJobStatus.STOPPED, categoryImportContext);

        this.importService.schedule(original);
        this.importService.start();

        this.importService.executeSeries(quota);

        this.categoriesServiceAdapterStub.assertCalledOnce(CategoryImportContextTest.CATEGORY_NAME);
        this.feedsServiceAdapterStub.assertCallOnce(FeedImportContextTest.HTTP_DOMAIN_COM, FeedImportContextTest.TITLE, categoryImportContext.getCategoryId());
    }

}
