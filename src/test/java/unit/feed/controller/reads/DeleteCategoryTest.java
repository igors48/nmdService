package unit.feed.controller.reads;

import nmd.rss.collector.controller.CategoryReport;
import nmd.rss.reader.Category;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * Created by igor on 19.03.2014.
 */
public class DeleteCategoryTest extends AbstractControllerTestBase {

    private static final String FIRST_NAME = "firstName";

    @Test
    public void whenCategoryIsDeletedThenItWillNotBeIncludedInReport() {
        final Category category = this.readsService.addCategory(FIRST_NAME);

        this.readsService.deleteCategory(category.uuid);

        final List<CategoryReport> reports = this.readsService.getCategoriesReport();
        final CategoryReport report = findForCategory(category.uuid, reports);

        assertNull(report);
    }

    //main category can not be deleted
}
