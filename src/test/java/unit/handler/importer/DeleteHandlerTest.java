package unit.handler.importer;

import nmd.orb.http.servlets.importer.ImportServletDeleteRequestHandler;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class DeleteHandlerTest {

    private ImportServiceWrapper importServiceWrapper;
    private ImportServletDeleteRequestHandler handler;

    private void setUp() {
        this.importServiceWrapper = Mockito.mock(ImportServiceWrapper.class);
        this.handler = new ImportServletDeleteRequestHandler(this.importServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.importServiceWrapper).reject();
        Mockito.verifyNoMoreInteractions(this.importServiceWrapper);

    }
}
