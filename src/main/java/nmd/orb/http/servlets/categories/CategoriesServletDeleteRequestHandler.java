package nmd.orb.http.servlets.categories;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import nmd.orb.http.wrappers.CategoriesServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.collector.error.ServiceError.invalidCategoryId;
import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.notNull;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.reader.Category.isValidCategoryId;

/**
 * @author : igu
 */
public class CategoriesServletDeleteRequestHandler implements Handler {

    public static final CategoriesServletDeleteRequestHandler CATEGORIES_SERVLET_DELETE_REQUEST_HANDLER = new CategoriesServletDeleteRequestHandler(CategoriesServiceWrapperImpl.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceWrapper categoriesService;

    public CategoriesServletDeleteRequestHandler(final CategoriesServiceWrapper categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    // DELETE -- /{categoryId} delete category
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidCategoryId(""));
        }

        final String first = elements.get(0);

        return isValidCategoryId(first) ? this.categoriesService.deleteCategory(first) : createErrorJsonResponse(invalidCategoryId(first));
    }

}
