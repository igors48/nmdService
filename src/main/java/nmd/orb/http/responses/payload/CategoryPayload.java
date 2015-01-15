package nmd.orb.http.responses.payload;

import nmd.orb.reader.Category;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryPayload that = (CategoryPayload) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public static CategoryPayload create(final Category category) {
        guard(notNull(category));

        final CategoryPayload categoryPayload = new CategoryPayload();

        categoryPayload.name = category.name;
        categoryPayload.id = category.uuid;

        return categoryPayload;
    }

}
