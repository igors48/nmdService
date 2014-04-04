package rest.categories;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.CategoryResponse;
import org.junit.Test;
import rest.AbstractRestTest;

import java.util.UUID;

/**
 * @author : igu
 */
public class DeleteCategoryTest extends AbstractRestTest {

    @Test
    public void whenNotExistentCategoryIsRemovedThenNoErrorReturns() {
        final String response = deleteCategory(UUID.randomUUID().toString());

        assertSuccessResponse(response);
    }

    @Test
    public void whenCategoryIdIsEmptyThenErrorReturns() {
        final String response = deleteCategory("");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_ID);
    }

    @Test
    public void whenCategoryIdIsInvalidThenErrorReturns() {
        final String response = deleteCategory("invalid");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_ID);
    }

    @Test
    public void whenExistentCategoryIsRemovedThenNoErrorReturns() {
        final CategoryResponse addCategoryResponse = addCategoryWithResponse("first");
        final String response = deleteCategory(addCategoryResponse.category.id);

        assertSuccessResponse(response);
    }

}
