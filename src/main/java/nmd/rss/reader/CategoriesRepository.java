package nmd.rss.reader;

import java.util.Set;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public interface CategoriesRepository {

    void store(Category category);

    Category load(String categoryId);

    //TODO analyse usages and replace with load
    Set<Category> loadAll();

    void delete(String categoryId);

    void clear();
}
