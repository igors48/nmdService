package unit.feed.controller.reads;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class AssignFeedToCategoryTest extends AbstractControllerTestBase {

    @Test
    public void whenFeedAddedThenItIsAssignedToMainCategory() throws ServiceException {
        final UUID feedId = addValidFirstRssFeed();

        final List<CategoryReport> categoryReports = this.readsService.getCategoriesReport();
        final CategoryReport main = CategoryAddingAndListingTest.findForCategory(Category.MAIN_CATEGORY_ID, categoryReports);

        assertTrue(main.feedIds.contains(feedId));
    }

    @Test
    public void whenFeedIsAssignedToSecondCategoryThenItIsUnAssignedFromMain() throws ServiceException {
        final Category secondCategory = this.readsService.addCategory("second");

        final UUID feedId = addValidFirstRssFeed();

        this.readsService.assignFeedToCategory(feedId, secondCategory.uuid);

        final List<CategoryReport> categoryReports = this.readsService.getCategoriesReport();
        final CategoryReport main = CategoryAddingAndListingTest.findForCategory(Category.MAIN_CATEGORY_ID, categoryReports);
        final CategoryReport second = CategoryAddingAndListingTest.findForCategory(secondCategory.uuid, categoryReports);

        assertFalse(main.feedIds.contains(feedId));
        assertTrue(second.feedIds.contains(feedId));
    }

    @Test(expected = ServiceException.class)
    public void whenFeedIsNotFoundThenExceptionOccurs() throws ServiceException {
        final Category secondCategory = this.readsService.addCategory("second");

        this.readsService.assignFeedToCategory(UUID.randomUUID(), secondCategory.uuid);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryIsNotFoundThenExceptionOccurs() throws ServiceException {
        final UUID feedId = addValidFirstRssFeed();

        this.readsService.assignFeedToCategory(feedId, UUID.randomUUID().toString());
    }

}
