package unit.feed.controller.categories;

import nmd.orb.collector.controller.CategoryReport;
import nmd.orb.collector.error.ServiceException;
import nmd.orb.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.orb.collector.util.Assert.assertNotNull;
import static nmd.orb.reader.Category.MAIN;
import static nmd.orb.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class RenameCategoryTest extends AbstractControllerTestBase {

    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";

    @Test
    public void whenCategoryIsRenamedThenItNewNameWillBeReturnedInReport() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        this.categoriesService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> report = this.categoriesService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(category.uuid, report);

        assertEquals(SECOND_NAME, renamed.name);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryWithNewNameAlreadyExistsThenExceptionWillBeThrown() throws ServiceException {
        final Category first = this.categoriesService.addCategory(FIRST_NAME);
        this.categoriesService.addCategory(SECOND_NAME);

        this.categoriesService.renameCategory(first.uuid, SECOND_NAME);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryWithGivenIdIsNotFoundThenExceptionWillBeThrown() throws ServiceException {
        this.categoriesService.renameCategory(randomUUID().toString(), SECOND_NAME);
    }

    @Test
    public void mainCategoryCanNotBeRenamed() throws ServiceException {
        this.categoriesService.renameCategory(MAIN_CATEGORY_ID, SECOND_NAME);

        final List<CategoryReport> report = this.categoriesService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(MAIN_CATEGORY_ID, report);

        assertEquals(MAIN.name, renamed.name);
    }

    @Test
    public void whenCategoryIsRenamedThenAllAssignedFeedWillBeRetained() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);
        final UUID firstFeedId = addValidFirstRssFeed(category.uuid);
        final UUID secondFeedId = addValidSecondRssFeed(category.uuid);

        this.categoriesService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> report = this.categoriesService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(category.uuid, report);

        assertNotNull(findForFeed(firstFeedId, renamed.feedReadReports));
        assertNotNull(findForFeed(secondFeedId, renamed.feedReadReports));
    }

    @Test
    public void whenCategoryIsRenamedThenCategoryWithItsOldNameNotExists() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        this.categoriesService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        for (final CategoryReport report : reports) {
            assertNotEquals(FIRST_NAME, report.name);
        }
    }

    @Test
    public void whenCategoryNewNameSameAsOldThenNothingWillBeChanged() throws ServiceException {
        final Category category = this.categoriesService.addCategory(FIRST_NAME);

        this.categoriesService.renameCategory(category.uuid, FIRST_NAME);

        final List<CategoryReport> reports = this.categoriesService.getCategoriesReport();

        assertEquals(2, reports.size());

        final CategoryReport renamed = findForCategory(category.uuid, reports);

        assertEquals(category.uuid, renamed.id);
        assertEquals(category.name, renamed.name);
    }

}
