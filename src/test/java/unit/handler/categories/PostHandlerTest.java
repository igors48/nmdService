package unit.handler.categories;

import nmd.orb.error.ErrorCode;
import nmd.orb.http.servlets.categories.CategoriesServletPostRequestHandler;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.assertError;
import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class PostHandlerTest {

    private CategoriesServiceWrapper categoriesServiceWrapper;
    private CategoriesServletPostRequestHandler handler;

    private void setUp() {
        this.categoriesServiceWrapper = Mockito.mock(CategoriesServiceWrapper.class);
        this.handler = new CategoriesServletPostRequestHandler(this.categoriesServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        assertError(call(this.handler, "/"), ErrorCode.INVALID_CATEGORY_NAME);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        assertError(call(this.handler, "/*"), ErrorCode.INVALID_CATEGORY_NAME);
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

        setUp();
        call(this.handler, "/", "category");
        Mockito.verify(this.categoriesServiceWrapper).addCategory("category");
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

    }
}
