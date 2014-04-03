package nmd.rss.collector.rest.responses;

import nmd.rss.collector.rest.responses.payload.CategoryPayload;
import nmd.rss.reader.Category;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoryResponse extends SuccessResponse {

    public CategoryPayload category;

    private CategoryResponse() {
        // empty
    }

    public static CategoryResponse create(final Category category) {
        guard(notNull(category));

        final CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.category = CategoryPayload.create(category);

        return categoryResponse;
    }

}
