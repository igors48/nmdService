package unit.feed.controller.stub;

import nmd.orb.reader.CategoriesRepository;
import nmd.orb.reader.Category;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class CategoriesRepositoryStub implements CategoriesRepository {

    private final Map<String, Category> categories;

    public CategoriesRepositoryStub() {
        this.categories = new HashMap<>();
    }

    @Override
    public void store(final Category category) {
        this.categories.put(category.uuid, category);
    }

    @Override
    public Category load(final String categoryId) {
        return this.categories.get(categoryId);
    }

    @Override
    public Set<Category> loadAll() {
        final Set<Category> categorySet = new HashSet<>();
        categorySet.addAll(this.categories.values());

        return categorySet;
    }

    @Override
    public void delete(final String categoryId) {
        this.categories.remove(categoryId);
    }

    @Override
    public void clear() {
        this.categories.clear();
    }

    public boolean isEmpty() {
        return this.categories.isEmpty();
    }

}
