package nmd.orb.services.change;

import nmd.orb.reader.Category;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class AddCategoryEvent implements Event {

    public final String categoryName;

    public AddCategoryEvent(final String categoryName) {
        guard(Category.isValidCategoryName(this.categoryName = categoryName));
    }

}
