package nmd.orb.http.responses;

import nmd.orb.http.responses.payload.CategoryPayload;
import nmd.orb.reader.Category;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.notNull;

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
