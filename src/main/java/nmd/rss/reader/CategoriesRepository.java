package nmd.rss.reader;

import java.util.Set;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.02.14
 */
public interface CategoriesRepository {

    void store(Category category);

    Set<Category> loadAll();

    void delete(String categoryId);
}
