package unit.handler.importer;

import com.google.gson.Gson;
import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.http.servlets.importer.ImportServletPostRequestHandler;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.report.ExportReport;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PostHandlerTest {

    private static final Gson GSON = new Gson();

    private ImportServiceWrapper importServiceWrapper;
    private ImportServletPostRequestHandler handler;

    private ExportReportResponse exportReportResponse;
    private ImportJobContext context;

    private void setUp() throws ServiceException {
        this.importServiceWrapper = Mockito.mock(ImportServiceWrapper.class);
        this.handler = new ImportServletPostRequestHandler(this.importServiceWrapper);

        final FeedHeader feedHeader = new FeedHeader(UUID.randomUUID(), "http://domain.com", "title", "description", "http://domain.com");
        final Set<FeedHeader> set = new HashSet<>();
        set.add(feedHeader);

        final Map<Category, Set<FeedHeader>> map = new HashMap<>();
        map.put(Category.MAIN, set);

        final ExportReport exportReport = new ExportReport(map, new HashSet<ReadFeedItems>(), new HashSet<FeedHeader>());
        this.exportReportResponse = ExportReportResponse.create(exportReport);

        this.context = ImportJobContext.convert(this.exportReportResponse.export, ImportServletPostRequestHandler.DEFAULT_TRIES_COUNT);
    }

    @Test
    public void routingTest() throws ServiceException {

        setUp();
        assertError(call(this.handler, "", "*"), ErrorCode.INVALID_IMPORT_FILE);
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

        setUp();
        assertError(call(this.handler, "", ""), ErrorCode.INVALID_IMPORT_FILE);
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);


        setUp();
        call(this.handler, "", GSON.toJson(this.exportReportResponse));
        Mockito.verify(this.importServiceWrapper).schedule(this.context);
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);
    }
}
