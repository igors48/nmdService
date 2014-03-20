package unit.feed.controller.categories;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class CategoryReportTest extends AbstractControllerTestBase {

    /*
        public final String id;
    public final String name;
    public final List<UUID> feedIds;
    public final int read;
    public final int notRead;
    public final int readLater;

     */

    @Test
    public void initialReportTest() {
        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(1, reports.size());

        final CategoryReport first = reports.get(0);

        assertEquals(Category.MAIN_CATEGORY_ID, first.id);
        assertEquals(Category.MAIN_CATEGORY_ID, first.name);
        assertTrue(first.feedIds.isEmpty());
        assertEquals(0, first.read);
        assertEquals(0, first.notRead);
        assertEquals(0, first.readLater);
    }

    @Test
    public void whenFeedStateWasChangedThenItWillBeReflectedInReport() throws ServiceException {
        final UUID feedId = addValidFirstRssFeedToMainCategory();

        this.readsService.markItemAsRead(feedId, FIRST_FEED_ITEM_GUID);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();
        final CategoryReport first = reports.get(0);

        assertEquals(Category.MAIN_CATEGORY_ID, first.id);
        assertEquals(Category.MAIN_CATEGORY_ID, first.name);
        assertTrue(first.feedIds.contains(feedId));
        assertEquals(1, first.read);
        assertEquals(2, first.notRead);
        assertEquals(0, first.readLater);
    }

    //when feed state changed it reflects in report
}
