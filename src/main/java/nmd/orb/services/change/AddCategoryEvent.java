package nmd.orb.services.change;

import nmd.orb.reader.Category;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class AddCategoryEvent implements Event {

    public final String categoryName;

    public AddCategoryEvent(final String categoryName) {
        guard(Category.isValidCategoryName(this.categoryName = categoryName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCategoryEvent that = (AddCategoryEvent) o;
        return Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

}
