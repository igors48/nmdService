package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportServiceScheduleTest extends AbstractImportServiceTest {

    @Test
    public void whenNoCurrentJobThenJobStored() throws ServiceException {
        final ImportJobContext job = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);

        this.importService.schedule(job);

        assertNotNull(this.feedImportJobRepositoryStub.load());
    }

    @Test
    public void whenCurrentJobStartedThenScheduleNewJobWillRaiseError() {

        try {
            final ImportJobContext current = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STARTED);
            this.feedImportJobRepositoryStub.store(current);

            final ImportJobContext job = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);
            this.importService.schedule(job);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.FEED_IMPORT_JOB_STARTED_ALREADY, exception.getError().code);
        }
    }

    @Test
    public void whenCurrentJobPausedThenScheduleNewJobWillReplaceIt() throws ServiceException {
        final ImportJobContext current = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);
        this.feedImportJobRepositoryStub.store(current);

        final ImportJobContext job = new ImportJobContext(new ArrayList<CategoryImportContext>(), ImportJobStatus.STOPPED);
        this.importService.schedule(job);

        assertEquals(job, this.feedImportJobRepositoryStub.load());
    }

}
