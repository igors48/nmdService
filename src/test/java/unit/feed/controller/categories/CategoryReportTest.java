package unit.feed.controller.categories;

import nmd.orb.collector.controller.CategoryReport;
import nmd.orb.collector.controller.FeedItemsReport;
import nmd.orb.error.ServiceException;
import nmd.orb.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class CategoryReportTest extends AbstractControllerTestBase {

    @Test
    public void initialReportTest() {
        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(1, reports.size());

        final CategoryReport first = reports.get(0);

        assertEquals(Category.MAIN_CATEGORY_ID, first.id);
        assertEquals(Category.MAIN_CATEGORY_ID, first.name);
        assertTrue(first.feedReadReports.isEmpty());
        assertEquals(0, first.read);
        assertEquals(0, first.notRead);
        assertEquals(0, first.readLater);
    }

    @Test
    public void whenFeedStateWasChangedThenItWillBeReflectedInReport() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedId);
        final String feedItemId = feedItemsReport.reports.get(0).itemId;

        this.readsService.markItemAsRead(feedId, feedItemId);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();
        final CategoryReport first = reports.get(0);

        assertEquals(Category.MAIN_CATEGORY_ID, first.id);
        assertEquals(Category.MAIN_CATEGORY_ID, first.name);
        assertNotNull(findForFeed(feedId, first.feedReadReports));
        assertEquals(1, first.read);
        assertEquals(1, first.notRead);
        assertEquals(0, first.readLater);
    }

    @Test
    public void whenCategoryIsFoundThenReportReturns() throws ServiceException {
        CategoryReport report = this.categoriesService.getCategoryReport(Category.MAIN_CATEGORY_ID);

        assertNotNull(report);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryIsNotFoundThenExceptionThrows() throws ServiceException {
        this.categoriesService.getCategoryReport(UUID.randomUUID().toString());
    }
}
