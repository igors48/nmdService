package nmd.rss.collector.controller;

import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;

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

    private final CategoriesRepository categoriesRepository;

    public CategoryService(final CategoriesRepository categoriesRepository) {
        assertNotNull(categoriesRepository);
        this.categoriesRepository = categoriesRepository;
    }

    public void addCategory(final String name) {
        assertStringIsValid(name);

        removeCategory(name);

        final Category category = new Category(UUID.randomUUID().toString(), name);

        this.categoriesRepository.store(category);
    }

    public void removeCategory(final String name) {
        assertStringIsValid(name);

        final Set<Category> categories = this.categoriesRepository.loadAll();

        for (final Iterator<Category> iterator = categories.iterator(); iterator.hasNext(); ) {
            final Category category = iterator.next();

            if (category.name.equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }
    }

    public Set<Category> getAllCategories() {
        return this.categoriesRepository.loadAll();
    }

}
