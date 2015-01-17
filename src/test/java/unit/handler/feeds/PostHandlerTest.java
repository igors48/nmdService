package unit.handler.feeds;

import nmd.orb.http.requests.AddFeedRequest;
import nmd.orb.http.servlets.feeds.FeedsServletPostRequestHandler;
import nmd.orb.http.wrappers.FeedsServiceWrapper;
import nmd.orb.reader.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * Created by igor on 17.01.2015.
 */
public class PostHandlerTest {

    public static final String HTTP_DOMAIN_COM = "http://domain.com";

    private FeedsServiceWrapper feedsServiceWrapper;
    private FeedsServletPostRequestHandler handler;

    @Before
    public void setUp() {
        this.feedsServiceWrapper = Mockito.mock(FeedsServiceWrapper.class);
        this.handler = new FeedsServletPostRequestHandler(this.feedsServiceWrapper);
    }

    @Test
    public void testName() throws Exception {
        final AddFeedRequest addFeedRequest = AddFeedRequest.create(HTTP_DOMAIN_COM, Category.MAIN_CATEGORY_ID);

        call(this.handler, "", addFeedRequest);

        Mockito.verify(this.feedsServiceWrapper).addFeed(HTTP_DOMAIN_COM, Category.MAIN_CATEGORY_ID);
    }

}
