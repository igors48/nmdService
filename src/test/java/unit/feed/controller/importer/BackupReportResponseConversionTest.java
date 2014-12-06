package unit.feed.controller.importer;

import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.BackupReportResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.report.BackupReport;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 06.12.2014.
 */
public class BackupReportResponseConversionTest {

    @Test
    public void roundtripTest() {
        final FeedHeader feedHeader = new FeedHeader(UUID.randomUUID(), "http://domain.com", "title", "description", "http://domain.com");
        final Set<FeedHeader> set = new HashSet<>();
        set.add(feedHeader);

        final Category category = new Category(UUID.randomUUID().toString(), "category");

        final Map<Category, Set<FeedHeader>> map = new HashMap<>();
        map.put(category, set);

        final BackupReport backupReport = new BackupReport(map, new HashSet<ReadFeedItems>(), new HashSet<FeedHeader>());
        final BackupReportResponse backupReportResponse = BackupReportResponse.create(backupReport);
        final ResponseBody responseBody = ResponseBody.createJsonFileResponse(backupReportResponse, "filename");

        final BackupReportResponse restoredResponse = BackupReportResponse.convert(responseBody.content);

        assertEquals(backupReportResponse, restoredResponse);
    }

}
