package nmd.rss.collector.rest.servlets.categories;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.CategoriesServiceInterface;
import nmd.rss.collector.rest.wrappers.CategoriesServiceWrapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * @author : igu
 */
public class CategoriesServletPutRequestHandler implements Handler {

    public static final CategoriesServletPutRequestHandler CATEGORIES_SERVLET_PUT_REQUEST_HANDLER = new CategoriesServletPutRequestHandler(CategoriesServiceWrapper.CATEGORIES_SERVICE_WRAPPER);

    private final CategoriesServiceInterface categoriesService;

    public CategoriesServletPutRequestHandler(final CategoriesServiceInterface categoriesService) {
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
