package unit.handler.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.http.servlets.importer.ImportServletPutRequestHandler;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PutHandlerTest {

    private ImportServiceWrapper importServiceWrapper;
    private ImportServletPutRequestHandler handler;

    private void setUp() throws ServiceException {
        this.importServiceWrapper = Mockito.mock(ImportServiceWrapper.class);
        this.handler = new ImportServletPutRequestHandler(this.importServiceWrapper);
    }

    @Test
    public void routingTest() throws ServiceException {

        setUp();
        assertError(call(this.handler, "/"), ErrorCode.FEED_IMPORT_JOB_INVALID_ACTION);
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.FEED_IMPORT_JOB_INVALID_ACTION);
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

        setUp();
        call(this.handler, "/start");
        Mockito.verify(this.importServiceWrapper).start();
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

        setUp();
        call(this.handler, "/stop");
        Mockito.verify(this.importServiceWrapper).stop();
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

    }
}
