package nmd.orb.services.change;

import nmd.orb.reader.Category;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenameCategoryEvent that = (RenameCategoryEvent) o;
        return Objects.equals(oldName, that.oldName) &&
                Objects.equals(newName, that.newName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldName, newName);
    }

}
