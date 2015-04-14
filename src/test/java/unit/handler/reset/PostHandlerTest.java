package unit.handler.reset;

import nmd.orb.http.servlets.reset.ResetServletPostRequestHandler;
import nmd.orb.http.wrappers.ResetServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PostHandlerTest {

    private ResetServiceWrapper resetServiceWrapper;
    private ResetServletPostRequestHandler handler;

    private void setUp() {
        this.resetServiceWrapper = Mockito.mock(ResetServiceWrapper.class);
        this.handler = new ResetServletPostRequestHandler(this.resetServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.resetServiceWrapper).clear();
        Mockito.verifyNoMoreInteractions(this.resetServiceWrapper);

    }
}
