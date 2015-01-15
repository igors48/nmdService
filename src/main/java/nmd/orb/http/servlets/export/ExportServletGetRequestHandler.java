package nmd.orb.http.servlets.export;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import nmd.orb.http.wrappers.CategoriesServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ExportServletGetRequestHandler implements Handler {

    public static final ExportServletGetRequestHandler EXPORT_SERVLET_GET_REQUEST_HANDLER = new ExportServletGetRequestHandler(CategoriesServiceWrapperImpl.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceWrapper categoriesService;

    public ExportServletGetRequestHandler(final CategoriesServiceWrapper categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    // GET -- get export report
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.categoriesService.createBackupReport();
    }

}
