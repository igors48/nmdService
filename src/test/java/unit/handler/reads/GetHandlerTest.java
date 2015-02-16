package unit.handler.reads;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.reads.ReadsServletGetRequestHandler;
import nmd.orb.http.wrappers.ReadsServiceWrapper;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.util.Direction;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * Created by igor on 06.02.2015.
 */
public class GetHandlerTest {

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
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225"), ErrorCode.INVALID_SIZE);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225/next"), ErrorCode.INVALID_PARAMETERS_COUNT);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225/wrong/5"), ErrorCode.INVALID_DIRECTION);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225/next/wrong"), ErrorCode.INVALID_SIZE);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_ALL);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=unknown");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_ALL);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=show-all");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_ALL);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=show-added");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_ADDED);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=show-not-read");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_NOT_READ);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=show-read-later");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_READ_LATER);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?filter=show-read-later");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), FeedItemReportFilter.SHOW_READ_LATER);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225/next/5");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsCardsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), "8efc756a-8dae-4ea7-8851-85706d1ef225", 5, Direction.NEXT);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/5");
        Mockito.verify(this.readsServiceWrapper).getFeedItemsCardsReport(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), "", 5, Direction.NEXT);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);
    }

}
