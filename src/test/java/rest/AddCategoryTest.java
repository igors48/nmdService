package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.CategoryResponse;
import org.junit.Test;

import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class AddCategoryTest extends AbstractRestTest {

    @Test
    public void whenCategoryNameValidThenCategoryWillBeCreatedAndReturned() {
        final CategoryResponse response = addCategoryWithResponse("category");

        assertTrue(isValidCategoryName(response.name));
        assertTrue(isValidCategoryId(response.id));
    }

    @Test
    public void whenCategoryNameInvalidThenErrorWillBeReturned() {
        final String response = addCategory("cate gory");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_NAME);
    }

}
