package http.importer;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.http.responses.FeedImportReportResponse;
import nmd.orb.services.report.ExportReport;
import org.junit.Test;
import unit.feed.controller.importer.ExportReportResponseConversionTest;

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
        final ExportReport exportReport = ExportReportResponseConversionTest.createBackupReport();
        final ExportReportResponse exportReportResponse = ExportReportResponse.create(exportReport);
        final String body = GSON.toJson(exportReportResponse);

        assertSuccessResponse(scheduleImportJob(body));
    }


    @Test
    public void whenSecondImportJobScheduledThenErrorResponseReturned() throws Exception {
        final ExportReport exportReport = ExportReportResponseConversionTest.createBackupReport();
        final ExportReportResponse exportReportResponse = ExportReportResponse.create(exportReport);
        final String body = GSON.toJson(exportReportResponse);

        scheduleImportJob(body);
        startFeedImportJob();
        assertErrorResponse(scheduleImportJob(body), ErrorCode.FEED_IMPORT_JOB_STARTED_ALREADY);
    }

    @Test
    public void whenImportJobIsEmptyThenErrorResponseReturned() throws Exception {
        assertErrorResponse(scheduleImportJob(""), ErrorCode.INVALID_IMPORT_FILE);
    }

    @Test
    public void whenImportJobIsIncorrectThenErrorResponseReturned() throws Exception {
        assertErrorResponse(scheduleImportJob("^&*^&*^&"), ErrorCode.INVALID_IMPORT_FILE);
    }

}
