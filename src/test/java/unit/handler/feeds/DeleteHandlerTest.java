package unit.handler.feeds;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.feeds.FeedsServletDeleteRequestHandler;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * Created by igor on 17.01.2015.
 */
public class DeleteHandlerTest {

    private FeedsServiceWrapper feedsServiceWrapper;
    private FeedsServletDeleteRequestHandler handler;

    @Before
    public void setUp() {
        this.feedsServiceWrapper = Mockito.mock(FeedsServiceWrapper.class);
        this.handler = new FeedsServletDeleteRequestHandler(this.feedsServiceWrapper);
    }

    @Test
    public void happyFlow() {
        final UUID uuid = UUID.randomUUID();

        call(this.handler, uuid.toString());

        Mockito.verify(this.feedsServiceWrapper).removeFeed(uuid);
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);
    }

    @Test
    public void whenFeedIdIsEmptyThenErrorReturns() {
        assertError(
                call(this.handler, ""),
                ErrorCode.INVALID_FEED_ID
        );
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);
    }

    @Test
    public void whenFeedIdIsInvalidThenErrorReturns() {
        assertError(
                call(this.handler, "invalid"),
                ErrorCode.INVALID_FEED_ID
        );
        Mockito.verifyNoMoreInteractions(this.feedsServiceWrapper);
    }

}
