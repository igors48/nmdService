package nmd.orb.reader;

import java.util.Set;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public interface CategoriesRepository {

    void store(Category category);

    Category load(String categoryId);

    Set<Category> loadAll();

    void delete(String categoryId);

    void clear();
}
