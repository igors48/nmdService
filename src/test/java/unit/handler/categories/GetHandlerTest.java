package unit.handler.categories;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.categories.CategoriesServletGetRequestHandler;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private CategoriesServiceWrapper categoriesServiceWrapper;
    private CategoriesServletGetRequestHandler handler;

    private void setUp() {
        this.categoriesServiceWrapper = Mockito.mock(CategoriesServiceWrapper.class);
        this.handler = new CategoriesServletGetRequestHandler(this.categoriesServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "/");
        Mockito.verify(this.categoriesServiceWrapper).getCategoriesReport();
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_CATEGORY_ID);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        call(this.handler, "/fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verify(this.categoriesServiceWrapper).getCategoryReport("fb5ea2da-2f60-4c11-9232-80bf50d49cf4");
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

    }
}
