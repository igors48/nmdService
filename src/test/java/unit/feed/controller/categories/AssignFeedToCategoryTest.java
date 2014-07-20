package unit.feed.controller.categories;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
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
