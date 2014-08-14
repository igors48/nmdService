package nmd.rss.collector.rest.wrappers;

import nmd.rss.collector.controller.CategoriesService;
import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.gae.persistence.GaeRootRepository;
import nmd.rss.collector.rest.responses.CategoriesReportResponse;
import nmd.rss.collector.rest.responses.CategoryReportResponse;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.reader.Category;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.rest.responses.SuccessMessageResponse.create;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ResponseBody.createJsonResponse;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServiceWrapper implements CategoriesServiceInterface {

    public static final CategoriesServiceWrapper CATEGORIES_SERVICE_WRAPPER = new CategoriesServiceWrapper(GaeRootRepository.CATEGORIES_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(CategoriesServiceWrapper.class.getName());

    private final CategoriesService categoriesService;

    public CategoriesServiceWrapper(final CategoriesService categoriesService) {
        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;
    }

    @Override
    public ResponseBody addCategory(final String name) {
        guard(isValidCategoryName(name));

        final Category category = this.categoriesService.addCategory(name);
        final CategoryResponse response = CategoryResponse.create(category);

        LOGGER.info(format("Category [ %s ] was created. It is id [ %s ]", category.name, category.uuid));

        return createJsonResponse(response);
    }

    @Override
    public ResponseBody getCategoriesReport() {
        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();
        final CategoriesReportResponse response = CategoriesReportResponse.create(reports);

        LOGGER.info("Categories report was created");

        return createJsonResponse(response);
    }

    @Override
    public ResponseBody getCategoryReport(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        try {
            final CategoryReport report = this.categoriesService.getCategoryReport(categoryId);
            final CategoryReportResponse response = CategoryReportResponse.create(report);

            LOGGER.info("Category report was created");

            return createJsonResponse(response);
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error creating report for category [ %s ]", categoryId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody assignFeedToCategory(final UUID feedId, final String categoryId) {
        guard(FeedHeader.isValidFeedHeaderId(feedId));
        guard(isValidCategoryId(categoryId));

        try {
            this.categoriesService.assignFeedToCategory(feedId, categoryId);

            final String message = format("Feed [ %s ] was assigned to category [ %s ]", feedId, categoryId);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error assigning feed [ %s ] to category [ %s ]", feedId, categoryId), exception);

            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody deleteCategory(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        this.categoriesService.deleteCategory(categoryId);

        final String message = format("Category [ %s ] removed", categoryId);

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    @Override
    public ResponseBody renameCategory(final String categoryId, final String newName) {
        guard(isValidCategoryId(categoryId));
        guard(isValidCategoryName(newName));

        try {
            this.categoriesService.renameCategory(categoryId, newName);

            final String message = format("Category [ %s ] name was changed to [ %s ]", categoryId, newName);

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error changing category [ %s ] name to [ %s ]", categoryId, newName), exception);

            return createErrorJsonResponse(exception);
        }
    }

}