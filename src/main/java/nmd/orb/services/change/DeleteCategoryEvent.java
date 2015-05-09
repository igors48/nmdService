package nmd.orb.services.change;

import nmd.orb.reader.Category;

import java.util.Objects;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class DeleteCategoryEvent implements Event {

    public final String name;

    public DeleteCategoryEvent(final String name) {
        guard(Category.isValidCategoryName(this.name = name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteCategoryEvent that = (DeleteCategoryEvent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
