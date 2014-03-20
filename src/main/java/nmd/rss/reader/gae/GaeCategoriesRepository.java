package nmd.rss.reader.gae;

import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;

import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class GaeCategoriesRepository implements CategoriesRepository {

    public static final CategoriesRepository GAE_CATEGORIES_REPOSITORY = new GaeCategoriesRepository();

    @Override
    public void store(final Category category) {
        assertNotNull(category);
    }

    @Override
    public Category load(final String categoryId) {
        assertStringIsValid(categoryId);

        return null;
    }

    @Override
    public Set<Category> loadAll() {
        return null;
    }

    @Override
    public void delete(final String categoryId) {
        assertStringIsValid(categoryId);
    }

    @Override
    public void clear() {

    }

}
