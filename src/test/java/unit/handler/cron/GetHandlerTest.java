package unit.handler.cron;

import nmd.orb.http.servlets.cron.CronServletGetRequestHandler;
import nmd.orb.http.wrappers.CronServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private CronServiceWrapper cronServiceWrapper;
    private CronServletGetRequestHandler handler;

    private void setUp() {
        this.cronServiceWrapper = Mockito.mock(CronServiceWrapper.class);
        this.handler = new CronServletGetRequestHandler(this.cronServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.cronServiceWrapper).executeCronJobs();
        Mockito.verifyNoMoreInteractions(this.cronServiceWrapper);

    }
}
