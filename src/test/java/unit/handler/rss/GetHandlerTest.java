package unit.handler.rss;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.rss.ExportRssServletGetRequestHandler;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private FeedsServiceWrapper feedsServiceWrapper;
    private ExportRssServletGetRequestHandler handler;

    private void setUp() {
        this.feedsServiceWrapper = Mockito.mock(FeedsServiceWrapper.class);
        this.handler = new ExportRssServletGetRequestHandler(this.feedsServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        assertError(call(this.handler, ""), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);

        setUp();
        call(this.handler, "/8efc756a-8dae-4ea7-8851-85706d1ef225");
        Mockito.verify(this.feedsServiceWrapper).getFeed(UUID.fromString("8efc756a-8dae-4ea7-8851-85706d1ef225"));
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);

    }
}
