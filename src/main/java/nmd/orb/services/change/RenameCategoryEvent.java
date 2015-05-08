package nmd.orb.services.change;

import nmd.orb.reader.Category;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RenameCategoryEvent implements Event {

    public final String oldName;
    public final String newName;

    public RenameCategoryEvent(final String oldName, final String newName) {
        guard(Category.isValidCategoryName(this.oldName = oldName));
        guard(Category.isValidCategoryName(this.newName = newName));
    }

}
