package nmd.rss.collector.rest;

import nmd.rss.collector.controller.CategoriesService;
import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.rest.responses.CategoriesReportResponse;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.reader.Category;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ResponseBody.createJsonResponse;
import static nmd.rss.collector.rest.responses.SuccessMessageResponse.create;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(CategoriesServiceWrapper.class.getName());

    private static final CategoriesService CATEGORIES_SERVICE =
            new CategoriesService(GAE_CACHED_CATEGORIES_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_TRANSACTIONS);

    public static ResponseBody addCategory(final String name) {
        guard(isValidCategoryName(name));

        final Category category = CATEGORIES_SERVICE.addCategory(name);
        final CategoryResponse response = CategoryResponse.create(category);

        LOGGER.info(format("Category [ %s ] was created. It is id [ %s ]", category.name, category.uuid));

        return createJsonResponse(response);
    }

    public static ResponseBody getCategoriesReport() {
        final List<CategoryReport> reports = CATEGORIES_SERVICE.getCategoriesReport();
        final CategoriesReportResponse response = CategoriesReportResponse.create(reports);

        LOGGER.info("Categories report was created");

        return createJsonResponse(response);
    }

    public static ResponseBody assignFeedToCategory(final UUID feedId, final String categoryId) {
        guard(FeedHeader.isValidFeedHeaderId(feedId));
        guard(isValidCategoryId(categoryId));

        try {
            CATEGORIES_SERVICE.assignFeedToCategory(feedId, categoryId);

            final String message = format("Feed [ %s ] was assigned to category [ %s ]", feedId, categoryId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error assigning feed [ %s ] to category [ %s ]", feedId, categoryId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    public static ResponseBody deleteCategory(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        CATEGORIES_SERVICE.deleteCategory(categoryId);

        final String message = format("Category [ %s ] removed", categoryId);

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    public static ResponseBody renameCategory(final String categoryId, final String newName) {
        guard(isValidCategoryId(categoryId));
        guard(isValidCategoryName(newName));

        try {
            CATEGORIES_SERVICE.renameCategory(categoryId, newName);

            final String message = format("Category [ %s ] name was changed to [ %s ]", categoryId, newName);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error changing category [ %s ] name to [ %s ]", categoryId, newName), exception);

            return createErrorJsonResponse(exception);
        }
    }

}