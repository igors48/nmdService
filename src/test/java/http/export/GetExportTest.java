package http.export;

import http.AbstractHttpTest;
import nmd.orb.http.responses.ExportReportResponse;
import org.junit.Test;

/**
 * Created by igor on 25.11.2014.
 */
public class GetExportTest extends AbstractHttpTest {

    @Test
    public void exportReportIsReturned() {
        final ExportReportResponse report = getExportReport();
    }

}
