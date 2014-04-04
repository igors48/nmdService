package rest.categories;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.reader.Category;
import org.junit.Test;
import rest.AbstractRestTest;

import java.util.UUID;

/**
 * @author : igu
 */
public class AssignFeedToCategoryTest extends AbstractRestTest {

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
