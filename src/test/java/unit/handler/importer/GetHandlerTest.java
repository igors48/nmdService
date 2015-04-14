package unit.handler.importer;

import nmd.orb.http.servlets.importer.ImportServletGetRequestHandler;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private ImportServiceWrapper importServiceWrapper;
    private ImportServletGetRequestHandler handler;

    private void setUp() {
        this.importServiceWrapper = Mockito.mock(ImportServiceWrapper.class);
        this.handler = new ImportServletGetRequestHandler(this.importServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.importServiceWrapper).status();
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

    }
}
