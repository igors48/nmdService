package unit.handler.categories;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.categories.CategoriesServletDeleteRequestHandler;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class DeleteHandlerTest {

    private CategoriesServiceWrapper categoriesServiceWrapper;
    private CategoriesServletDeleteRequestHandler handler;

    private void setUp() {
        this.categoriesServiceWrapper = Mockito.mock(CategoriesServiceWrapper.class);
        this.handler = new CategoriesServletDeleteRequestHandler(this.categoriesServiceWrapper);
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
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verify(this.categoriesServiceWrapper).deleteCategory("fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

    }
}
