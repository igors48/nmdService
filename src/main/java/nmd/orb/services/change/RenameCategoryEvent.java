package nmd.orb.services.change;

import nmd.orb.reader.Category;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class RenameCategoryEvent implements Event {

    public final String oldCategoryName;
    public final String newCategoryName;

    public RenameCategoryEvent(final String oldCategoryName, final String newCategoryName) {
        guard(Category.isValidCategoryName(this.oldCategoryName = oldCategoryName));
        guard(Category.isValidCategoryName(this.newCategoryName = newCategoryName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenameCategoryEvent that = (RenameCategoryEvent) o;
        return Objects.equals(oldCategoryName, that.oldCategoryName) &&
                Objects.equals(newCategoryName, that.newCategoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldCategoryName, newCategoryName);
    }

}
