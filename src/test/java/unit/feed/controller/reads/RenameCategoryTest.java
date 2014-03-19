package unit.feed.controller.reads;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.rss.reader.Category.MAIN;
import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14
 */
public class RenameCategoryTest extends AbstractControllerTestBase {

    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";

    @Test
    public void whenCategoryIsRenamedThenItNewNameWillBeReturnedInReport() throws ServiceException {
        final Category category = this.readsService.addCategory(FIRST_NAME);

        this.readsService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> report = this.readsService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(category.uuid, report);

        assertEquals(SECOND_NAME, renamed.name);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryWithNewNameAlreadyExistsThenExceptionWillBeThrown() throws ServiceException {
        final Category first = this.readsService.addCategory(FIRST_NAME);
        this.readsService.addCategory(SECOND_NAME);

        this.readsService.renameCategory(first.uuid, SECOND_NAME);
    }

    @Test(expected = ServiceException.class)
    public void whenCategoryWithGivenIdIsNotFoundThenExceptionWillBeThrown() throws ServiceException {
        this.readsService.renameCategory(randomUUID().toString(), SECOND_NAME);
    }

    @Test
    public void mainCategoryCanNotBeRenamed() throws ServiceException {
        this.readsService.renameCategory(MAIN_CATEGORY_ID, SECOND_NAME);

        final List<CategoryReport> report = this.readsService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(MAIN_CATEGORY_ID, report);

        assertEquals(MAIN.name, renamed.name);
    }

    @Test
    public void whenCategoryIsRenamedThenAllAssignedFeedWillBeRetained() throws ServiceException {
        final Category category = this.readsService.addCategory(FIRST_NAME);
        final UUID firstFeedId = addValidFirstRssFeed(category.uuid);
        final UUID secondFeedId = addValidSecondRssFeed(category.uuid);

        this.readsService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> report = this.readsService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(category.uuid, report);

        assertTrue(renamed.feedIds.contains(firstFeedId));
        assertTrue(renamed.feedIds.contains(secondFeedId));
    }

    /*
    @Test
    public void whenCategoryIsRenamedThenItNewNameWillBeReturnedInReport() throws ServiceException {
        final Category category = this.readsService.addCategory(FIRST_NAME);

        this.readsService.renameCategory(category.uuid, SECOND_NAME);

        final List<CategoryReport> report = this.readsService.getCategoriesReport();
        final CategoryReport renamed = findForCategory(category.uuid, report);

        assertEquals(SECOND_NAME, renamed.name);
    }
    */
    //check handling of wrong names
    //if new name same as old - nothing changed

}
