package unit.feed.controller.categories;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.03.14
 */
public class CategoryAddingAndListingTest extends AbstractControllerTestBase {

    public static final String NEW_CATEGORY_NAME = "new";

    @Test
    public void whenEverythingStartsThenOnlyEmptyMainCategoryExists() {
        final List<CategoryReport> categoriesReport = this.categoriesService.getCategoriesReport();

        assertEquals(1, categoriesReport.size());
        assertEquals(Category.MAIN_CATEGORY_ID, categoriesReport.get(0).id);
        assertTrue(categoriesReport.get(0).feedReadReports.isEmpty());
    }

    @Test
    public void whenCategoryWasAddedThenItWillBeReturnInSetAsEmpty() {
        final Category created = this.categoriesService.addCategory(NEW_CATEGORY_NAME);

        final List<CategoryReport> categoriesReport = this.categoriesService.getCategoriesReport();

        assertEquals(2, categoriesReport.size());
        assertTrue(findForCategory(created.uuid, categoriesReport).feedReadReports.isEmpty());
    }

    @Test
    public void whenTryToCreateAnotherCategoryWithSameNameThenPreviousCategoryReturns() {
        final Category first = this.categoriesService.addCategory(NEW_CATEGORY_NAME);
        final Category second = this.categoriesService.addCategory(NEW_CATEGORY_NAME);

        assertEquals(first, second);
        assertEquals(2, this.categoriesService.getCategoriesReport().size());
    }

    @Test
    public void whenTryToCreateMainCategoryThenMainCategoryReturns() {
        final Category created = this.categoriesService.addCategory(Category.MAIN.name);

        assertEquals(Category.MAIN, created);
        assertEquals(1, this.categoriesService.getCategoriesReport().size());
    }

    @Test
    public void categoriesInDifferentCasesAreTreatedAsTheSameAndNoDuplicatesWillBeCreated() {
        final Category first = this.categoriesService.addCategory(NEW_CATEGORY_NAME);
        final Category diffCase = this.categoriesService.addCategory(NEW_CATEGORY_NAME.toUpperCase());

        assertEquals(first, diffCase);
        assertEquals(2, this.categoriesService.getCategoriesReport().size());
    }

}
