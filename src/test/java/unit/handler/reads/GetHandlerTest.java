package unit.handler.reads;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.reads.ReadsServletGetRequestHandler;
import nmd.orb.http.wrappers.ReadsServiceWrapper;
import nmd.orb.services.filter.FeedItemReportFilter;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * Created by igor on 06.02.2015.
 */
public class GetHandlerTest {

    private static final UUID FEED_ID = UUID.randomUUID();
    private static final String ITEM_ID = UUID.randomUUID().toString();

    private ReadsServiceWrapper readsServiceWrapper;
    private ReadsServletGetRequestHandler handler;

    public void setUp() {
        this.readsServiceWrapper = Mockito.mock(ReadsServiceWrapper.class);
        this.handler = new ReadsServletGetRequestHandler(this.readsServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.readsServiceWrapper).getFeedsReadReport();

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_FEED_ID);

        setUp();
        call(this.handler, "/" + FEED_ID);
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_ALL);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=unknown");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_ALL);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=show-all");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_ALL);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=show-added");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_ADDED);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=show-not-read");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_NOT_READ);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=show-read-later");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_READ_LATER);

        setUp();
        call(this.handler, "/" + FEED_ID + "?filter=show-read-later");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(FEED_ID, FeedItemReportFilter.SHOW_READ_LATER);

        setUp();
        assertError(call(this.handler, "/" + FEED_ID + "/" + ITEM_ID), ErrorCode.INVALID_PARAMETERS_COUNT);

        setUp();
        assertError(call(this.handler, "/" + FEED_ID + "/" + ITEM_ID + "/next"), ErrorCode.INVALID_PARAMETERS_COUNT);
    }

}
