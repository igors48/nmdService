package nmd.orb.http.responses.payload;

import nmd.orb.reader.Category;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoryPayload {

    public String name;
    public String id;

    private CategoryPayload() {
        // empty
    }

    public static CategoryPayload create(final Category category) {
        guard(notNull(category));

        final CategoryPayload categoryPayload = new CategoryPayload();

        categoryPayload.name = category.name;
        categoryPayload.id = category.uuid;

        return categoryPayload;
    }

}
