package nmd.rss.reader;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public class Category {

    public static final String MAIN_CATEGORY_ID = "main";
    public static final Category MAIN = new Category(MAIN_CATEGORY_ID, MAIN_CATEGORY_ID);

    public final String uuid;
    public final String name;

    public Category(final String uuid, final String name) {
        assertStringIsValid(uuid);
        this.uuid = uuid;

        assertStringIsValid(name);
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

}
