package nmd.orb.reader;

import nmd.orb.collector.Cache;

import java.util.Set;
import java.util.logging.Logger;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.notNull;
import static nmd.orb.reader.Category.isValidCategoryId;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 10.03.14
 */
public class CachedCategoriesRepository implements CategoriesRepository {

    public static final String KEY = "Categories";

    private static final Logger LOGGER = Logger.getLogger(CachedCategoriesRepository.class.getName());

    private final CategoriesRepository repository;
    private final Cache cache;

    public CachedCategoriesRepository(final CategoriesRepository repository, final Cache cache) {
        guard(notNull(repository));
        this.repository = repository;

        guard(notNull(cache));
        this.cache = cache;
    }

    @Override
    public synchronized void store(final Category category) {
        guard(notNull(category));

        this.repository.store(category);

        this.cache.delete(KEY);
    }

    @Override
    public synchronized Category load(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        final Set<Category> categories = loadAll();

        for (Category category : categories) {

            if (category.uuid.equals(categoryId)) {
                return category;
            }
        }

        return null;
    }

    @Override
    public synchronized Set<Category> loadAll() {
        final Set<Category> cached = (Set<Category>) this.cache.get(KEY);

        return cached == null ? loadCache() : cached;
    }

    @Override
    public synchronized void delete(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        this.repository.delete(categoryId);

        this.cache.delete(KEY);
    }

    @Override
    public synchronized void clear() {
        this.cache.delete(KEY);
        this.repository.clear();
    }

    private Set<Category> loadCache() {
        final Set<Category> categories = this.repository.loadAll();

        this.cache.put(KEY, categories);

        LOGGER.info("Categories were loaded from datastore");

        return categories;
    }

}
