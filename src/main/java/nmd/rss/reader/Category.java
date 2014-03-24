package nmd.rss.reader;

import java.io.Serializable;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public class Category implements Serializable {

    public static final String MAIN_CATEGORY_ID = "main";
    public static final Category MAIN = new Category(MAIN_CATEGORY_ID, MAIN_CATEGORY_ID);

    private static final int MAXIMUM_CATEGORY_NAME_LENGTH = 16;

    public final String uuid;
    public final String name;

    public Category(final String uuid, final String name) {
        guard(isValidCategoryId(uuid));
        this.uuid = uuid;

        guard(isValidCategoryName(name));
        this.name = name;
    }

    public Category changeName(final String newName) {
        return new Category(this.uuid, newName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;
        if (!uuid.equals(category.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public static boolean isValidCategoryName(final String value) {

        if (!isValidString(value)) {
            return false;
        }

        if (value.length() > MAXIMUM_CATEGORY_NAME_LENGTH) {
            return false;
        }

        if (!isContainOnlyFileNameChars(value)) {
            return false;
        }

        return true;
    }

    public static boolean isValidCategoryId(final String value) {

        if (!isValidString(value)) {
            return false;
        }

        if (!(value.equals(Category.MAIN_CATEGORY_ID) || isVaildUuid(value))) {
            return false;
        }

        return true;
    }

}
