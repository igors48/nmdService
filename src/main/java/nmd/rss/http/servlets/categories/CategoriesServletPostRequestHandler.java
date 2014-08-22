package nmd.rss.http.servlets.categories;

import nmd.rss.http.Handler;
import nmd.rss.http.tools.ResponseBody;
import nmd.rss.http.wrappers.CategoriesServiceWrapper;
import nmd.rss.http.wrappers.CategoriesServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.error.ServiceError.invalidCategoryName;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * @author : igu
 */
public class CategoriesServletPostRequestHandler implements Handler {

    public static final CategoriesServletPostRequestHandler CATEGORIES_SERVLET_POST_REQUEST_HANDLER = new CategoriesServletPostRequestHandler(CategoriesServiceWrapperImpl.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceWrapper categoriesService;

    public CategoriesServletPostRequestHandler(final CategoriesServiceWrapper categoriesService) {
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
