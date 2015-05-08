package nmd.orb.services.change;

import nmd.orb.reader.Category;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class DeleteCategoryEvent implements Event {

    public final String name;

    public DeleteCategoryEvent(final String name) {
        guard(Category.isValidCategoryName(this.name = name));
    }

}
