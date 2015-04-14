package unit.handler.updates;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.updates.UpdatesServletGetRequestHandler;
import nmd.orb.http.wrappers.UpdatesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private UpdatesServiceWrapper updatesServiceWrapper;
    private UpdatesServletGetRequestHandler handler;

    private void setUp() {
        this.updatesServiceWrapper = Mockito.mock(UpdatesServiceWrapper.class);
        this.handler = new UpdatesServletGetRequestHandler(this.updatesServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        assertError(call(this.handler, ""), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.updatesServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.updatesServiceWrapper);

        setUp();
        call(this.handler, "/8efc756a-8dae-4ea7-8851-85706d1ef225");
        Mockito.verify(this.updatesServiceWrapper).updateFeed(UUID.fromString("8efc756a-8dae-4ea7-8851-85706d1ef225"));
        Mockito.verifyNoMoreInteractions(this.updatesServiceWrapper);

    }

}
