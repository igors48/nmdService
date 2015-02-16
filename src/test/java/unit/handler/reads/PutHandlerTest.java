package unit.handler.reads;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.reads.ReadsServletPutRequestHandler;
import nmd.orb.http.wrappers.ReadsServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * Created by igor on 15.02.2015.
 */
public class PutHandlerTest {

    private ReadsServiceWrapper readsServiceWrapper;
    private ReadsServletPutRequestHandler handler;

    public void setUp() {
        this.readsServiceWrapper = Mockito.mock(ReadsServiceWrapper.class);
        this.handler = new ReadsServletPutRequestHandler(this.readsServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        assertError(call(this.handler, ""), ErrorCode.INVALID_FEED_OR_ITEM_ID);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?topItemTimestamp=48");
        Mockito.verify(this.readsServiceWrapper).markAllItemsAsRead(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), 48);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4?topItemTimestamp=trash");
        Mockito.verify(this.readsServiceWrapper).markAllItemsAsRead(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), 0);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verify(this.readsServiceWrapper).markAllItemsAsRead(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), 0);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225"), ErrorCode.INVALID_MARK_MODE);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225?mark-as=trash"), ErrorCode.INVALID_MARK_MODE);
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225?markAs=read");
        Mockito.verify(this.readsServiceWrapper).markItemAsRead(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), "8efc756a-8dae-4ea7-8851-85706d1ef225");
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225?markAs=readLater");
        Mockito.verify(this.readsServiceWrapper).toggleItemAsReadLater(UUID.fromString("fb5ea2da-2f60-4c11-9232-80bf50d49cf4"), "8efc756a-8dae-4ea7-8851-85706d1ef225");
        Mockito.verifyNoMoreInteractions(this.readsServiceWrapper);

    }

}
