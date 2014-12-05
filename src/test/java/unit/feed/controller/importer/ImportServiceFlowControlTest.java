package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author : igu
 */
public class ImportServiceFlowControlTest extends AbstractImportServiceTest {

    @Test
    public void whenJobStartedThenItsStatusChanged() throws ServiceException {
        final ImportJobContext context = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);

        this.importService.schedule(context);
        this.importService.start();

        assertEquals(ImportJobStatus.STARTED, this.feedImportJobRepositoryStub.load().getStatus());
    }

    @Test
    public void whenJobStoppedThenItsStatusChanged() throws ServiceException {
        final ImportJobContext context = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);

        this.importService.schedule(context);
        this.importService.start();
        this.importService.stop();

        assertEquals(ImportJobStatus.STOPPED, this.feedImportJobRepositoryStub.load().getStatus());
    }

    @Test
    public void whenJobRejectedThenItsRemovedFromRepository() throws ServiceException {
        final ImportJobContext context = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);

        this.importService.schedule(context);
        this.importService.reject();

        assertNull(this.feedImportJobRepositoryStub.load());
    }

    @Test
    public void whenJobExecutedThenUpdatedContextStoredInRepository() throws ServiceException {
        final ImportJobContext original = ImportJobContextTest.create(ImportJobStatus.STOPPED, CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE));

        this.importService.schedule(original);
        this.importService.start();
        this.importService.executeOne();
        this.importService.executeOne();
        this.importService.executeOne();
        this.importService.executeOne();

        final ImportJobContext updated = this.feedImportJobRepositoryStub.load();

        assertEquals(ImportJobStatus.COMPLETED, updated.getStatus());
    }

}
