package http.importer;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.FeedImportReportResponse;
import org.junit.Test;

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

}
