package unit.handler.export;

import nmd.orb.http.servlets.export.ExportServletGetRequestHandler;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import org.junit.Test;
import org.mockito.Mockito;

import static unit.handler.Tools.call;

/**
 * @author : igu
 */
public class GetHandlerTest {

    private CategoriesServiceWrapper categoriesServiceWrapper;
    private ExportServletGetRequestHandler handler;

    private void setUp() {
        this.categoriesServiceWrapper = Mockito.mock(CategoriesServiceWrapper.class);
        this.handler = new ExportServletGetRequestHandler(this.categoriesServiceWrapper);
    }

    @Test
    public void routingTest() {

        setUp();
        call(this.handler, "");
        Mockito.verify(this.categoriesServiceWrapper).createExportReport();
        Mockito.verifyNoMoreInteractions(this.categoriesServiceWrapper);

    }
}

