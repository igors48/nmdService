package unit.handler.reset;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.administration.AdministrationServletPostRequestHandler;
import nmd.orb.http.wrappers.AdministrationServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PostHandlerTest {

    private AdministrationServiceWrapper administrationServiceWrapper;
    private AdministrationServletPostRequestHandler handler;

    private void setUp() {
        this.administrationServiceWrapper = Mockito.mock(AdministrationServiceWrapper.class);
        this.handler = new AdministrationServletPostRequestHandler(this.administrationServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, AdministrationServletPostRequestHandler.FULL_RESET);
        Mockito.verify(this.administrationServiceWrapper).clear();
        Mockito.verifyNoMoreInteractions(this.administrationServiceWrapper);

        setUp();
        call(this.handler, AdministrationServletPostRequestHandler.CACHE_RESET);
        Mockito.verify(this.administrationServiceWrapper).flushCache();
        Mockito.verifyNoMoreInteractions(this.administrationServiceWrapper);

        setUp();
        assertError(call(this.handler, ""), ErrorCode.INVALID_RESET_ACTION);
        Mockito.verifyNoMoreInteractions(this.administrationServiceWrapper);

        setUp();
        assertError(call(this.handler, "invalid"), ErrorCode.INVALID_RESET_ACTION);
        Mockito.verifyNoMoreInteractions(this.administrationServiceWrapper);

    }
}
