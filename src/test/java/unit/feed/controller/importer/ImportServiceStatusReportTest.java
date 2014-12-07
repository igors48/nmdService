package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.FeedImportTaskStatus;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import nmd.orb.services.report.FeedImportStatusReport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 07.12.2014.
 */
public class ImportServiceStatusReportTest extends AbstractImportServiceTest {

    @Test
    public void whenThereIsNoContextThenZeroReportReturned() {
        this.feedImportJobRepositoryStub.store(null);

        final FeedImportStatusReport report = this.importService.status();

        assertEquals(FeedImportStatusReport.DEFAULT, report);
    }

    @Test
    public void whenContextExistsThenReportReturned() throws ServiceException {
        ImportJobContext context = ImportJobContextTest.create(ImportJobStatus.STOPPED,
                CategoryImportContextTest.create(CategoryImportTaskStatus.CATEGORY_CREATE,
                        FeedImportContextTest.create(3, FeedImportTaskStatus.WAITING)));
        this.importService.schedule(context);
        final FeedImportStatusReport report = this.importService.status();

        final FeedImportStatusReport expected = new FeedImportStatusReport(1, 0, 0);

        assertEquals(expected, report);
    }

}
