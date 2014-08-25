package nmd.orb.http.servlets.categories;

import nmd.orb.feed.FeedHeader;
import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.CategoriesServiceWrapper;
import nmd.orb.http.wrappers.CategoriesServiceWrapperImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.orb.collector.error.ServiceError.*;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CategoriesServletPutRequestHandler implements Handler {

    public static final CategoriesServletPutRequestHandler CATEGORIES_SERVLET_PUT_REQUEST_HANDLER = new CategoriesServletPutRequestHandler(CategoriesServiceWrapperImpl.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceWrapper categoriesService;

    public CategoriesServletPutRequestHandler(final CategoriesServiceWrapper categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    // PUT -- /{categoryId} rename category
    // PUT -- /{categoryId}/{feedId} assign feed to category
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidCategoryId(""));
        }

        final String categoryId = elements.get(0);

        if (!isValidCategoryId(categoryId)) {
            return createErrorJsonResponse(invalidCategoryId(categoryId));
        }

        if (elements.size() == 1) {

            if (!isValidCategoryName(body)) {
                return createErrorJsonResponse(invalidCategoryName(body));
            }

            return this.categoriesService.renameCategory(categoryId, body);
        } else {
            final String feedId = elements.get(1);

            if (!FeedHeader.isValidFeedHeaderId(feedId)) {
                return createErrorJsonResponse(invalidFeedId(feedId));
            }

            return this.categoriesService.assignFeedToCategory(UUID.fromString(feedId), categoryId);
        }
    }

}
