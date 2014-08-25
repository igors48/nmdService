package nmd.orb.http.wrappers;

import nmd.orb.collector.controller.CategoriesService;
import nmd.orb.collector.controller.CategoryReport;
import nmd.orb.collector.error.ServiceException;
import nmd.orb.collector.feed.FeedHeader;
import nmd.orb.gae.GaeServices;
import nmd.orb.http.responses.CategoriesReportResponse;
import nmd.orb.http.responses.CategoryReportResponse;
import nmd.orb.http.responses.CategoryResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.reader.Category;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServiceWrapperImpl implements CategoriesServiceWrapper {

    public static final CategoriesServiceWrapperImpl CATEGORIES_SERVICE_WRAPPER = new CategoriesServiceWrapperImpl(GaeServices.CATEGORIES_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(CategoriesServiceWrapperImpl.class.getName());

    private final CategoriesService categoriesService;

    public CategoriesServiceWrapperImpl(final CategoriesService categoriesService) {
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