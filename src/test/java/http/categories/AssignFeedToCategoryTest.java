package http.categories;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.CategoryResponse;
import nmd.orb.http.responses.FeedIdResponse;
import nmd.orb.reader.Category;
import org.junit.Test;

import java.util.UUID;

/**
 * @author : igu
 */
public class AssignFeedToCategoryTest extends AbstractHttpTest {

    @Test
    public void whenEverythingIsOkThenSuccessReturns() {
        final FeedIdResponse feedIdResponse = addFeedWithResponse(FIRST_FEED_URL, Category.MAIN_CATEGORY_ID);
        final CategoryResponse categoryResponse = addCategoryWithResponse("category");

        final String response = assignFeedToCategory(categoryResponse.category.id, feedIdResponse.feedId.toString());

        assertSuccessResponse(response);
    }

    @Test
    public void whenFeedIdIsNotValidThenErrorReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse("category");

        final String response = assignFeedToCategory(categoryResponse.category.id, "invalid");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenFeedIdIsUnknownThenErrorReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse("category");

        final String response = assignFeedToCategory(categoryResponse.category.id, UUID.randomUUID().toString());

        assertErrorResponse(response, ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenCategoryIdIsNotValidThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFeedWithResponse(FIRST_FEED_URL, Category.MAIN_CATEGORY_ID);

        final String response = assignFeedToCategory("invalid", feedIdResponse.feedId.toString());

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_ID);
    }

    @Test
    public void whenCategoryIdIsUnknownThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFeedWithResponse(FIRST_FEED_URL, Category.MAIN_CATEGORY_ID);

        final String response = assignFeedToCategory(UUID.randomUUID().toString(), feedIdResponse.feedId.toString());

        assertErrorResponse(response, ErrorCode.WRONG_CATEGORY_ID);
    }

}
