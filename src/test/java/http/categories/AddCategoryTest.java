package http.categories;

import http.AbstractHttpTest;
import nmd.orb.collector.error.ErrorCode;
import nmd.orb.http.responses.CategoryResponse;
import org.junit.Test;

import static nmd.orb.reader.Category.isValidCategoryId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class AddCategoryTest extends AbstractHttpTest {

    public static final String CATEGORY = "category";

    @Test
    public void whenCategoryNameValidThenCategoryWillBeCreatedAndReturned() {
        final CategoryResponse response = addCategoryWithResponse(CATEGORY);

        assertEquals(CATEGORY, response.category.name);
        assertTrue(isValidCategoryId(response.category.id));
    }

    @Test
    public void whenCategoryNameInvalidThenErrorWillBeReturned() {
        final String response = addCategory("cate gory");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_NAME);
    }

}
