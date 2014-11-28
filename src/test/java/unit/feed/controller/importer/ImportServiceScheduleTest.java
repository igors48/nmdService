package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.services.ImportService;
import nmd.orb.services.importer.FeedImportJob;
import nmd.orb.services.importer.FeedImportJobStatus;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.FeedImportJobRepositoryStub;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ImportServiceScheduleTest {

    private FeedImportJobRepositoryStub feedImportJobRepositoryStub;
    private ImportService importService;

    @Before
    public void setUp() {
        this.feedImportJobRepositoryStub = new FeedImportJobRepositoryStub();

        this.importService = new ImportService(this.feedImportJobRepositoryStub);
    }

    @Test
    public void whenNoCurrentJobThenJobStored() throws ServiceException {
        final FeedImportJob job = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.PAUSED);

        this.importService.schedule(job);

        assertNotNull(this.feedImportJobRepositoryStub.load());
    }

    @Test
    public void whenCurrentJobStartedThenScheduleNewJobWillRaiseError() {

        try {
            final FeedImportJob current = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.STARTED);
            this.feedImportJobRepositoryStub.store(current);

            final FeedImportJob job = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.PAUSED);
            this.importService.schedule(job);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.FEED_IMPORT_JOB_STARTED_ALREADY, exception.getError().code);
        }
    }

    @Test
    public void whenCurrentJobPausedThenScheduleNewJobWillReplaceIt() throws ServiceException {
        final FeedImportJob current = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.PAUSED);
        this.feedImportJobRepositoryStub.store(current);

        final FeedImportJob job = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.PAUSED);
        this.importService.schedule(job);

        assertEquals(job, this.feedImportJobRepositoryStub.load());
    }

}
