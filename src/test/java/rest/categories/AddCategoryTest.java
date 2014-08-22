package rest.categories;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.http.responses.CategoryResponse;
import org.junit.Test;
import rest.AbstractRestTest;

import static nmd.rss.reader.Category.isValidCategoryId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class AddCategoryTest extends AbstractRestTest {

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
