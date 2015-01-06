package http.importer;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.BackupReportResponse;
import nmd.orb.http.responses.FeedImportReportResponse;
import nmd.orb.services.report.BackupReport;
import org.junit.Test;
import unit.feed.controller.importer.BackupReportResponseConversionTest;

/**
 * @author : igu
 */
public class ImportJobControlTest extends AbstractHttpTest {

    @Test
    public void importJobStatusReturned() {
        final FeedImportReportResponse response = getFeedImportReport();
    }

    @Test
    public void whenImportJobDeletedThenSuccessResponseReturned() {
        deleteFeedImportJob();
    }

    @Test
    public void whenImportJobStartedThenSuccessResponseReturned() {
        startFeedImportJob();
    }

    @Test
    public void whenImportJobStoppedThenSuccessResponseReturned() {
        stopFeedImportJob();
    }

    @Test
    public void whenImportJobActionIsEmptyThenErrorResponseReturned() {
        final String response = sendFeedImportJobAction("");

        assertErrorResponse(response, ErrorCode.FEED_IMPORT_JOB_INVALID_ACTION);
    }

    @Test
    public void whenImportJobActionIsInvalidThenErrorResponseReturned() {
        final String response = sendFeedImportJobAction("asdf");

        assertErrorResponse(response, ErrorCode.FEED_IMPORT_JOB_INVALID_ACTION);
    }

    @Test
    public void whenImportJobScheduledThenSuccessResponseReturned() throws Exception {
        final BackupReport backupReport = BackupReportResponseConversionTest.createBackupReport();
        final BackupReportResponse backupReportResponse = BackupReportResponse.create(backupReport);
        final String body = GSON.toJson(backupReportResponse);

        assertSuccessResponse(scheduleImportJob(body));
    }


    @Test
    public void whenSecondImportJobScheduledThenErrorResponseReturned() throws Exception {
        final BackupReport backupReport = BackupReportResponseConversionTest.createBackupReport();
        final BackupReportResponse backupReportResponse = BackupReportResponse.create(backupReport);
        final String body = GSON.toJson(backupReportResponse);

        scheduleImportJob(body);
        startFeedImportJob();
        assertErrorResponse(scheduleImportJob(body), ErrorCode.FEED_IMPORT_JOB_STARTED_ALREADY);
    }

    @Test
    public void whenImportJobIsEmptyThenErrorResponseReturned() throws Exception {
        assertErrorResponse(scheduleImportJob(""), ErrorCode.INVALID_BACKUP_FILE);
    }

    @Test
    public void whenImportJobIsIncorrectThenErrorResponseReturned() throws Exception {
        assertErrorResponse(scheduleImportJob("^&*^&*^&"), ErrorCode.INVALID_BACKUP_FILE);
    }

}
