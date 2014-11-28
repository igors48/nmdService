package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.FeedImportJob;
import nmd.orb.services.importer.FeedImportJobStatus;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class ImportServiceFlowControlTest extends AbstractImportServiceTest {

    @Test
    public void whenJobStartedThenItsStatusChanged() throws ServiceException {
        final FeedImportJob job = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.STOPPED);

        this.importService.schedule(job);
        this.importService.start();

        assertEquals(FeedImportJobStatus.STARTED, this.feedImportJobRepositoryStub.load().status);
    }

    @Test
    public void whenJobStoppedThenItsStatusChanged() throws ServiceException {
        final FeedImportJob job = new FeedImportJob(UUID.randomUUID(), FeedImportJobStatus.STOPPED);

        this.importService.schedule(job);
        this.importService.start();
        this.importService.stop();

        assertEquals(FeedImportJobStatus.STOPPED, this.feedImportJobRepositoryStub.load().status);
    }

}
