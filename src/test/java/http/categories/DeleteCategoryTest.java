package http.categories;

import http.AbstractHttpTest;
import nmd.orb.collector.error.ErrorCode;
import nmd.orb.http.responses.CategoryResponse;
import org.junit.Test;

import java.util.UUID;

/**
 * @author : igu
 */
public class DeleteCategoryTest extends AbstractHttpTest {

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
