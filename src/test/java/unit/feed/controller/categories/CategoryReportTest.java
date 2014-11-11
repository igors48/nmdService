package unit.feed.controller.categories;

import nmd.orb.error.ServiceException;
import nmd.orb.reader.Category;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.services.report.CategoryReport;
import nmd.orb.services.report.FeedItemsReport;
import nmd.orb.services.report.FeedReadReport;
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

        final FeedItemsReport feedItemsReport = this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL);
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
        final CategoryReport report = this.categoriesService.getCategoryReport(Category.MAIN_CATEGORY_ID);

        assertNotNull(report);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryIsNotFoundThenExceptionThrows() throws ServiceException {
        this.categoriesService.getCategoryReport(UUID.randomUUID().toString());
    }

    @Test
    public void whenReportContainsCategoriesThenTheyAreSortedAlphabeticallyByName() {
        this.categoriesService.addCategory("first");
        this.categoriesService.addCategory("zet");

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals("first", reports.get(0).name);
        assertEquals(Category.MAIN.name, reports.get(1).name);
        assertEquals("zet", reports.get(2).name);
    }

    @Test
    public void whenCategoryReportCreatedThenFeedsAreSortedAlphabeticallyByTitle() throws ServiceException {
        final UUID secondFeedId = addValidSecondRssFeedToMainCategory();
        final UUID firstFeedId = addValidFirstRssFeedToMainCategory();

        final CategoryReport report = this.categoriesService.getCategoryReport(Category.MAIN_CATEGORY_ID);
        final List<FeedReadReport> readReports = report.feedReadReports;

        assertEquals(firstFeedId, readReports.get(0).feedId);
        assertEquals(secondFeedId, readReports.get(1).feedId);
    }

}
