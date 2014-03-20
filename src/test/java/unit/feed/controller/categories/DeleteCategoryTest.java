package unit.feed.controller.categories;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 19.03.14
 */
public class DeleteCategoryTest extends AbstractControllerTestBase {

    private static final String FIRST_NAME = "firstName";

    @Test
    public void whenCategoryIsDeletedThenItWillNotBeIncludedInReport() {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        this.categoriesService.deleteCategory(category.uuid);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();
        final CategoryReport report = findForCategory(category.uuid, reports);

        assertNull(report);
    }

    @Test
    public void mainCategoryCanNotBeDeleted() {
        this.categoriesService.deleteCategory(MAIN_CATEGORY_ID);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(1, reports.size());

        final CategoryReport report = findForCategory(MAIN_CATEGORY_ID, reports);

        assertNotNull(report);
    }

    @Test
    public void whenNotExistsCategoryIsDeletedThenNothingWillHappened() {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        this.categoriesService.deleteCategory(randomUUID().toString());

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(2, reports.size());

        assertNotNull(findForCategory(category.uuid, reports));
        assertNotNull(findForCategory(MAIN_CATEGORY_ID, reports));
    }

    @Test
    public void whenCategoryIsDeletedThenAllTheyFeedsWillBeAssignedToMainCategory() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        final UUID firstFeedId = addValidFirstRssFeed(category.uuid);
        final UUID secondFeedId = addValidSecondRssFeed(category.uuid);

        this.categoriesService.deleteCategory(category.uuid);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(1, reports.size());

        final CategoryReport report = findForCategory(MAIN_CATEGORY_ID, reports);

        assertTrue(report.feedIds.contains(firstFeedId));
        assertTrue(report.feedIds.contains(secondFeedId));
    }

}
