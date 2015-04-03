package unit.handler.categories;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.categories.CategoriesServletPutRequestHandler;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PutHandlerTest {

    private CategoriesServiceWrapper categoriesServiceWrapper;
    private CategoriesServletPutRequestHandler handler;

    private void setUp() {
        this.categoriesServiceWrapper = Mockito.mock(CategoriesServiceWrapper.class);
        this.handler = new CategoriesServletPutRequestHandler(this.categoriesServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        assertError(call(this.handler, "/"), ErrorCode.INVALID_CATEGORY_ID);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_CATEGORY_ID);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4", "category");
        Mockito.verify(this.categoriesServiceWrapper).renameCategory("fb5ea2da-2f60-4c11-9232-80bf50d49cf4", "category");
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        assertError(call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/*"), ErrorCode.INVALID_FEED_ID);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4/8efc756a-8dae-4ea7-8851-85706d1ef225", "category");
        Mockito.verify(this.categoriesServiceWrapper).assignFeedToCategory(UUID.fromString("8efc756a-8dae-4ea7-8851-85706d1ef225"), "fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

    }
}
