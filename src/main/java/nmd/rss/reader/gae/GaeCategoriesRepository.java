package nmd.rss.reader.gae;

import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;

import java.util.Set;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class GaeCategoriesRepository implements CategoriesRepository {

    public static final CategoriesRepository GAE_CATEGORIES_REPOSITORY = new GaeCategoriesRepository();

    @Override
    public void store(Category category) {

    }

    @Override
    public Set<Category> loadAll() {
        return null;
    }

    @Override
    public void delete(String categoryId) {

    }

    @Override
    public void clear() {

    }

}
