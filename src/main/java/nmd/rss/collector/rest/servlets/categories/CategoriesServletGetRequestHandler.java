package nmd.rss.collector.rest.servlets.categories;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.CategoriesServiceWrapper;
import nmd.rss.collector.rest.wrappers.CategoriesServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.error.ServiceError.invalidCategoryId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.reader.Category.isValidCategoryId;

/**
 * @author : igu
 */
public class CategoriesServletGetRequestHandler implements Handler {

    public static final CategoriesServletGetRequestHandler CATEGORIES_SERVLET_GET_REQUEST_HANDLER = new CategoriesServletGetRequestHandler(CategoriesServiceWrapperImpl.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceWrapper categoriesService;

    public CategoriesServletGetRequestHandler(final CategoriesServiceWrapper categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    // GET -- get categories report
    // GET -- /{categoryId} get category report
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return this.categoriesService.getCategoriesReport();
        }

        final String first = elements.get(0);

        return isValidCategoryId(first) ? this.categoriesService.getCategoryReport(first) : createErrorJsonResponse(invalidCategoryId(first));
    }

}
