package http.backup;

import http.AbstractHttpTest;
import nmd.orb.http.responses.BackupReportResponse;
import org.junit.Test;

/**
 * Created by igor on 25.11.2014.
 */
public class GetBackupTest extends AbstractHttpTest {

    @Test
    public void backupReportIsReturned() {
        final BackupReportResponse report = getBackupReport();
    }

}
