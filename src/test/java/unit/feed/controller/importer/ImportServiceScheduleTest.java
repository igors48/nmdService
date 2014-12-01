package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.ImportJob;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportServiceScheduleTest extends AbstractImportServiceTest {

    @Test
    public void whenNoCurrentJobThenJobStored() throws ServiceException {
        final ImportJob job = new ImportJob(UUID.randomUUID(), ImportJobStatus.STOPPED);

        this.importService.schedule(job);

        assertNotNull(this.feedImportJobRepositoryStub.load());
    }

    @Test
    public void whenCurrentJobStartedThenScheduleNewJobWillRaiseError() {

        try {
            final ImportJob current = new ImportJob(UUID.randomUUID(), ImportJobStatus.STARTED);
            this.feedImportJobRepositoryStub.store(current);

            final ImportJob job = new ImportJob(UUID.randomUUID(), ImportJobStatus.STOPPED);
            this.importService.schedule(job);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.FEED_IMPORT_JOB_STARTED_ALREADY, exception.getError().code);
        }
    }

    @Test
    public void whenCurrentJobPausedThenScheduleNewJobWillReplaceIt() throws ServiceException {
        final ImportJob current = new ImportJob(UUID.randomUUID(), ImportJobStatus.STOPPED);
        this.feedImportJobRepositoryStub.store(current);

        final ImportJob job = new ImportJob(UUID.randomUUID(), ImportJobStatus.STOPPED);
        this.importService.schedule(job);

        assertEquals(job, this.feedImportJobRepositoryStub.load());
    }

}
