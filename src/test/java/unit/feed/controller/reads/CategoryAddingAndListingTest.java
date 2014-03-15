package unit.feed.controller.reads;

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
        final List<CategoryReport> categoriesReport = this.readsService.getCategoriesReport();

        assertEquals(1, categoriesReport.size());
        assertEquals(Category.MAIN_CATEGORY_ID, categoriesReport.get(0).id);
        assertTrue(categoriesReport.get(0).feedIds.isEmpty());
    }

    @Test
    public void whenCategoryWasAddedThenItWillBeReturnInSetAsEmpty() {
        final Category created = this.readsService.addCategory(NEW_CATEGORY_NAME);

        final List<CategoryReport> categoriesReport = this.readsService.getCategoriesReport();

        assertEquals(2, categoriesReport.size());
        assertTrue(findForCategory(created.uuid, categoriesReport).feedIds.isEmpty());
    }

    @Test
    public void whenTryToCreateAnotherCategoryWithSameNameThenPreviousCategoryReturns() {
        final Category first = this.readsService.addCategory(NEW_CATEGORY_NAME);
        final Category second = this.readsService.addCategory(NEW_CATEGORY_NAME);

        assertEquals(first, second);
        assertEquals(2, this.readsService.getCategoriesReport().size());
    }

    @Test
    public void whenTryToCreateMainCategoryThenMainCategoryReturns() {
        final Category created = this.readsService.addCategory(Category.MAIN.name);

        assertEquals(Category.MAIN, created);
        assertEquals(1, this.readsService.getCategoriesReport().size());
    }

    @Test
    public void categoriesWithSpacesAtTheEndsOrInDifferentCasesAreTreatedAsTheSameAndNoDuplicatesWillBeCreated() {
        final Category first = this.readsService.addCategory(NEW_CATEGORY_NAME);
        final Category spaceBefore = this.readsService.addCategory(" " + NEW_CATEGORY_NAME);
        final Category spaceAfter = this.readsService.addCategory(NEW_CATEGORY_NAME + " ");
        final Category diffCase = this.readsService.addCategory(" " + NEW_CATEGORY_NAME.toUpperCase());

        assertEquals(first, spaceAfter);
        assertEquals(first, spaceBefore);
        assertEquals(first, diffCase);
        assertEquals(2, this.readsService.getCategoriesReport().size());
    }

    static CategoryReport findForCategory(final String categoryId, final List<CategoryReport> categoriesReport) {

        for (final CategoryReport categoryReport : categoriesReport) {

            if (categoryReport.id.equals(categoryId)) {
                return categoryReport;
            }

        }

        return null;
    }

}
