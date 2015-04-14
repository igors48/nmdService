package unit.handler.content;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.content.ContentServletGetRequestHandler;
import nmd.orb.http.wrappers.ContentFilterServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private ContentFilterServiceWrapper contentServiceWrapper;
    private ContentServletGetRequestHandler handler;

    private void setUp() {
        this.contentServiceWrapper = Mockito.mock(ContentFilterServiceWrapper.class);
        this.handler = new ContentServletGetRequestHandler(this.contentServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "?link=http://domain.com");
        Mockito.verify(this.contentServiceWrapper).filter("http://domain.com");
        Mockito.verifyNoMoreInteractions(this.contentServiceWrapper);

        setUp();
        assertError(call(this.handler, ""), ErrorCode.INVALID_URL);
        Mockito.verifyNoMoreInteractions(this.contentServiceWrapper);

        setUp();
        assertError(call(this.handler, "?link=*"), ErrorCode.INVALID_URL);
        Mockito.verifyNoMoreInteractions(this.contentServiceWrapper);

        setUp();
        assertError(call(this.handler, "?link="), ErrorCode.INVALID_URL);
        Mockito.verifyNoMoreInteractions(this.contentServiceWrapper);
    }

}

