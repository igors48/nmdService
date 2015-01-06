package http.importer;

import http.AbstractHttpTest;
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

}
