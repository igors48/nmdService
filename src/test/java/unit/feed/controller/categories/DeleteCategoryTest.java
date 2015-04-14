package unit.feed.controller.categories;

import nmd.orb.error.ServiceException;
import nmd.orb.reader.Category;
import nmd.orb.services.report.CategoryReport;
import org.junit.Test;
import org.mockito.Mockito;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.orb.reader.Category.MAIN_CATEGORY_ID;
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

        assertNotNull(findForFeed(firstFeedId, report.feedReadReports));
        assertNotNull(findForFeed(secondFeedId, report.feedReadReports));
    }

    @Test
    public void whenCategoryIsDeletedThenItIsRegistered() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);
        this.categoriesService.deleteCategory(category.uuid);

        Mockito.verify(this.changeRegistrationServiceSpy, Mockito.times(2)).registerChange();
    }

}
