package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author : igu
 */
public class ImportServiceFlowControlTest extends AbstractImportServiceTest {

    @Test
    public void whenJobStartedThenItsStatusChanged() throws ServiceException {
        final ImportJobContext job = new ImportJobContext(UUID.randomUUID(), ImportJobStatus.STOPPED);

        this.importService.schedule(job);
        this.importService.start();

        assertEquals(ImportJobStatus.STARTED, this.feedImportJobRepositoryStub.load().getStatus());
    }

    @Test
    public void whenJobStoppedThenItsStatusChanged() throws ServiceException {
        final ImportJobContext job = new ImportJobContext(UUID.randomUUID(), ImportJobStatus.STOPPED);

        this.importService.schedule(job);
        this.importService.start();
        this.importService.stop();

        assertEquals(ImportJobStatus.STOPPED, this.feedImportJobRepositoryStub.load().getStatus());
    }

    @Test
    public void whenJobRejectedThenItsRemovedFromRepository() throws ServiceException {
        final ImportJobContext job = new ImportJobContext(UUID.randomUUID(), ImportJobStatus.STOPPED);

        this.importService.schedule(job);
        this.importService.reject();

        assertNull(this.feedImportJobRepositoryStub.load());
    }

}
