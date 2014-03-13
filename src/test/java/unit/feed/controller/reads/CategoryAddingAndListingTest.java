package unit.feed.controller.reads;

import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.03.14
 */
public class CategoryAddingAndListingTest extends AbstractControllerTestBase {

    public static final String NEW_CATEGORY_NAME = "new";

    @Test
    public void whenEverythingStartsThenOnlyMainCategoryExists() {
        final Set<Category> categories = this.readsService.getAllCategories();

        assertEquals(1, categories.size());
        assertTrue(categories.contains(Category.MAIN));
    }

    @Test
    public void whenCategoryWasAddedThenItWillBeReturnInSet() {
        final Category created = this.readsService.addCategory(NEW_CATEGORY_NAME);

        final Set<Category> categories = this.readsService.getAllCategories();

        assertEquals(2, categories.size());
        assertTrue(categories.contains(created));
    }

    @Test
    public void whenTryToCreateAnotherCategoryWithSameNameThenPreviousCategoryReturns() {
        final Category first = this.readsService.addCategory(NEW_CATEGORY_NAME);
        final Category second = this.readsService.addCategory(NEW_CATEGORY_NAME);

        assertEquals(first, second);
        assertEquals(2, this.readsService.getAllCategories().size());
    }

    @Test
    public void whenTryToCreateMainCategoryThenMainCategoryReturns() {
        final Category created = this.readsService.addCategory(Category.MAIN.name);

        assertEquals(Category.MAIN, created);
        assertEquals(1, this.readsService.getAllCategories().size());
    }

    @Test
    public void categoriesWithSpacesAtTheEndsOrInDifferentCasesAreThreatedAsTheSameAndNoDuplicatesWillBeCreated() {
        final Category first = this.readsService.addCategory(NEW_CATEGORY_NAME);
        final Category spaceBefore = this.readsService.addCategory(" " + NEW_CATEGORY_NAME);
        final Category spaceAfter = this.readsService.addCategory(NEW_CATEGORY_NAME + " ");
        final Category diffCase = this.readsService.addCategory(" " + NEW_CATEGORY_NAME.toUpperCase());

        assertEquals(first, spaceAfter);
        assertEquals(first, spaceBefore);
        assertEquals(first, diffCase);
        assertEquals(2, this.readsService.getAllCategories().size());
    }

}
