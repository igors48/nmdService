package unit.feed.controller.categories;

import nmd.orb.error.ServiceException;
import nmd.orb.reader.Category;
import nmd.orb.services.report.CategoryReport;
import org.junit.Test;
import org.mockito.Mockito;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class AssignFeedToCategoryTest extends AbstractControllerTestBase {

    @Test
    public void whenFeedIsAssignedToSecondCategoryThenItIsUnAssignedFromFirst() throws ServiceException {
        final Category firstCategory = this.categoriesService.addCategory("first");
        final Category secondCategory = this.categoriesService.addCategory("second");

        final UUID feedId = addValidFirstRssFeed(firstCategory.uuid);

        this.categoriesService.assignFeedToCategory(feedId, secondCategory.uuid);

        final List<CategoryReport> categoryReports = this.categoriesService.getCategoriesReport();
        final CategoryReport main = findForCategory(firstCategory.uuid, categoryReports);
        final CategoryReport second = findForCategory(secondCategory.uuid, categoryReports);

        assertNull(findForFeed(feedId, main.feedReadReports));
        assertNotNull(findForFeed(feedId, second.feedReadReports));
    }

    @Test
    public void whenFeedIsAssignedToCategoryThenItIsRegistered() throws ServiceException {
        final Category firstCategory = this.categoriesService.addCategory("first");
        addValidFirstRssFeed(firstCategory.uuid);

        Mockito.verify(this.autoExportServiceSpy, Mockito.times(2)).registerChange();
    }

    @Test(expected = ServiceException.class)
    public void whenFeedIsNotFoundThenExceptionOccurs() throws ServiceException {
        final Category secondCategory = this.categoriesService.addCategory("second");

        this.categoriesService.assignFeedToCategory(UUID.randomUUID(), secondCategory.uuid);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryIsNotFoundThenExceptionOccurs() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.categoriesService.assignFeedToCategory(feedId, UUID.randomUUID().toString());
    }

}
