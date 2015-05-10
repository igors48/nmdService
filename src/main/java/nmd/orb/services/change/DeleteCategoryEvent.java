package nmd.orb.services.change;

import nmd.orb.reader.Category;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class DeleteCategoryEvent implements Event {

    public final String categoryName;

    public DeleteCategoryEvent(final String categoryName) {
        guard(Category.isValidCategoryName(this.categoryName = categoryName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteCategoryEvent that = (DeleteCategoryEvent) o;
        return Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

}
