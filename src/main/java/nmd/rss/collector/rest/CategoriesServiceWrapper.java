package nmd.rss.collector.rest;

import nmd.rss.collector.controller.CategoriesService;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.reader.Category;

import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.rest.ResponseBody.createJsonResponse;
import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServiceWrapper {

    private static final CategoriesService CATEGORIES_SERVICE =
            new CategoriesService(GAE_CACHED_CATEGORIES_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_TRANSACTIONS);

    public static ResponseBody addCategory(final String name) {
        guard(isValidCategoryName(name));

        final Category category = CATEGORIES_SERVICE.addCategory(name);
        final CategoryResponse response = CategoryResponse.convert(category);

        return createJsonResponse(response);
    }

    public static ResponseBody getCategoriesReport() {
        return null;
    }

    public static ResponseBody assignFeedToCategory(final UUID feedId, final String categoryId) {
        assertNotNull(feedId);
        assertStringIsValid(categoryId);

        return null;
    }

    public static ResponseBody deleteCategory(final String categoryId) {
        assertStringIsValid(categoryId);

        return null;
    }

    public static ResponseBody renameCategory(final String categoryId, final String newName) {
        assertStringIsValid(categoryId);
        assertStringIsValid(newName);

        return null;
    }

}