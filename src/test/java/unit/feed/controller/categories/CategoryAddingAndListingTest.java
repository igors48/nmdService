package unit.feed.controller.categories;

import nmd.orb.reader.Category;
import nmd.orb.services.report.CategoryReport;
import org.junit.Test;
import org.mockito.Mockito;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;

import static nmd.orb.reader.Category.isValidCategoryId;
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
    public void whenCategoryWasAddedThenItIsRegistered() {
        this.categoriesService.addCategory(NEW_CATEGORY_NAME);

        Mockito.verify(this.changeRegistrationServiceSpy).registerAddCategory(NEW_CATEGORY_NAME);
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

    @Test
    public void whenCategoryCreatedThenItsIdReturned() {
        final String categoryId = this.categoriesService.createCategory(NEW_CATEGORY_NAME);
        assertTrue(isValidCategoryId(categoryId));
    }

    @Test
    public void whenCategoryCreatedThenItIsRegistered() {
        this.categoriesService.createCategory(NEW_CATEGORY_NAME);

        Mockito.verify(this.changeRegistrationServiceSpy).registerAddCategory(NEW_CATEGORY_NAME);
    }

    @Test
    public void whenCategoryAlreadyExistsThenItsIdReturned() {
        final Category first = this.categoriesService.addCategory(NEW_CATEGORY_NAME);
        final String categoryId = this.categoriesService.createCategory(NEW_CATEGORY_NAME);

        assertEquals(first.uuid, categoryId);
    }
}
