package rest.categories;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.CategoryResponse;
import org.junit.Test;
import rest.AbstractRestTest;

/**
 * @author : igu
 */
public class RenameCategoryTest extends AbstractRestTest {

    private static final String CATEGORY_NAME = "category";
    private static final String NEW_NAME = "renamed";

    @Test
    public void whenEverythingIsFineThenSuccessReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        final String response = renameCategory(categoryResponse.category.id, NEW_NAME);

        assertSuccessResponse(response);
    }

    @Test
    public void whenNewNameIsEmptyThenErrorReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        final String response = renameCategory(categoryResponse.category.id, "");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_NAME);
    }

    @Test
    public void whenNewNameIsInvalidThenErrorReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        final String response = renameCategory(categoryResponse.category.id, "*");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_NAME);
    }

    @Test
    public void whenCategoryIdIsInvalidThenErrorReturns() {
        addCategoryWithResponse(CATEGORY_NAME);
        final String response = renameCategory("invalid", NEW_NAME);

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_ID);
    }

}
