package nmd.rss.collector.controller;

import nmd.rss.reader.Category;
import nmd.rss.reader.CategoryRepository;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.02.14
 */
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        assertNotNull(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(final String name) {
        assertStringIsValid(name);

        removeCategory(name);

        final Category category = new Category(UUID.randomUUID().toString(), name);

        this.categoryRepository.store(category);
    }

    public void removeCategory(final String name) {
        assertStringIsValid(name);

        final Set<Category> categories = this.categoryRepository.loadAll();

        for (final Iterator<Category> iterator = categories.iterator(); iterator.hasNext(); ) {
            final Category category = iterator.next();

            if (category.name.equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }
    }

    public Set<Category> getAllCategories() {
        return this.categoryRepository.loadAll();
    }

}
