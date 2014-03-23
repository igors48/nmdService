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

    public CategoryResponse(final Category category) {
        super();

        guard(notNull(category));
        this.category = new CategoryPayload(category);
    }

}
