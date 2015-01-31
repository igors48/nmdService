package unit.handler.feeds;

import nmd.orb.http.servlets.feeds.FeedsServletPutRequestHandler;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.call;

/**
 * Created by igor on 31.01.2015.
 */
public class PutHandlerTest {

    private FeedsServiceWrapper feedsServiceWrapper;
    private FeedsServletPutRequestHandler handler;

    @Before
    public void setUp() {
        this.feedsServiceWrapper = Mockito.mock(FeedsServiceWrapper.class);
        this.handler = new FeedsServletPutRequestHandler(this.feedsServiceWrapper);
    }

    @Test
    public void happyFlow() {
        final UUID feedId = UUID.randomUUID();
        final String title = "title";

        call(this.handler, feedId.toString(), title);

        Mockito.verify(this.feedsServiceWrapper).updateFeedTitle(feedId, title);
    }

}
