package unit.handler.reset;

import nmd.orb.http.servlets.reset.ResetServletPostRequestHandler;
import nmd.orb.http.wrappers.AdministrationServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PostHandlerTest {

    private AdministrationServiceWrapper administrationServiceWrapper;
    private ResetServletPostRequestHandler handler;

    private void setUp() {
        this.administrationServiceWrapper = Mockito.mock(AdministrationServiceWrapper.class);
        this.handler = new ResetServletPostRequestHandler(this.administrationServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.administrationServiceWrapper).clear();
        Mockito.verifyNoMoreInteractions(this.administrationServiceWrapper);

    }
}
