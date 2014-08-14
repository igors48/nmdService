package nmd.rss.collector.rest.servlets.categories;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.CategoriesServiceInterface;
import nmd.rss.collector.rest.wrappers.CategoriesServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.error.ServiceError.invalidCategoryName;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * @author : igu
 */
public class CategoriesServletPostRequestHandler implements Handler {

    public static final CategoriesServletPostRequestHandler CATEGORIES_SERVLET_POST_REQUEST_HANDLER = new CategoriesServletPostRequestHandler(CategoriesServiceWrapper.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceInterface categoriesService;

    public CategoriesServletPostRequestHandler(final CategoriesServiceInterface categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    // POST -- add category
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return (isValidCategoryName(body)) ? this.categoriesService.addCategory(body) : createErrorJsonResponse(invalidCategoryName(body));
    }

}
