package unit.handler.feeds;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.feeds.FeedsServletGetRequestHandler;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * Created by igor on 31.01.2015.
 */
public class GetHandlerTest {

    private FeedsServiceWrapper feedsServiceWrapper;
    private FeedsServletGetRequestHandler handler;

    @Before
    public void setUp() {
        this.feedsServiceWrapper = Mockito.mock(FeedsServiceWrapper.class);
        this.handler = new FeedsServletGetRequestHandler(this.feedsServiceWrapper);
    }

    @Test
    public void whenNoFeedIdThenAllHeadersReturns() {
        call(this.handler, "");

        Mockito.verify(this.feedsServiceWrapper).getFeedHeaders();
    }

    @Test
    public void whenFeedIdIsCorrectThenHeaderReturns() {
        final UUID feedId = UUID.randomUUID();

        call(this.handler, feedId.toString());

        Mockito.verify(this.feedsServiceWrapper).getFeedHeader(feedId);
    }

    @Test
    public void whenFeedIdIsNotCorrectThenErrorReturns() {
        assertError(
                call(this.handler, "invalid"),
                ErrorCode.INVALID_FEED_ID
        );
    }

}
