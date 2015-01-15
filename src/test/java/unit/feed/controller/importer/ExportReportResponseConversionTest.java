package unit.feed.controller.importer;

import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.report.ExportReport;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 06.12.2014.
 */
public class ExportReportResponseConversionTest {

    @Test
    public void roundtripTest() {
        final ExportReport exportReport = createBackupReport();
        final ExportReportResponse exportReportResponse = ExportReportResponse.create(exportReport);
        final ResponseBody responseBody = ResponseBody.createJsonFileResponse(exportReportResponse, "filename");

        final ExportReportResponse restoredResponse = ExportReportResponse.convert(responseBody.content);

        assertEquals(exportReportResponse, restoredResponse);
    }

    public static ExportReport createBackupReport() {
        final FeedHeader feedHeader = new FeedHeader(UUID.randomUUID(), "http://domain.com", "title", "description", "http://domain.com");
        final Set<FeedHeader> set = new HashSet<>();
        set.add(feedHeader);

        final Category category = new Category(UUID.randomUUID().toString(), "category");

        final Map<Category, Set<FeedHeader>> map = new HashMap<>();
        map.put(category, set);

        return new ExportReport(map, new HashSet<ReadFeedItems>(), new HashSet<FeedHeader>());
    }

}
