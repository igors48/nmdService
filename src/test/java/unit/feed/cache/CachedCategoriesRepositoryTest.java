package unit.feed.cache;

import nmd.orb.reader.CachedCategoriesRepository;
import nmd.orb.reader.Category;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.CategoriesRepositoryStub;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class CachedCategoriesRepositoryTest {

    private static final String CATEGORY_ID = UUID.randomUUID().toString();
    private static final Category STORED = new Category(CATEGORY_ID, "stored");
    private static final Category CACHED = new Category(CATEGORY_ID, "cached");

    private CategoriesRepositoryStub categoriesRepositoryStub;
    private CacheStub cache;

    private CachedCategoriesRepository cachedCategoriesRepository;

    @Before
    public void setUp() {
        this.cache = new CacheStub();
        this.cache.put(CachedCategoriesRepository.KEY, new HashSet<Category>() {{
            add(CACHED);
        }});

        this.categoriesRepositoryStub = new CategoriesRepositoryStub();
        this.categoriesRepositoryStub.store(STORED);

        this.cachedCategoriesRepository = new CachedCategoriesRepository(this.categoriesRepositoryStub, this.cache);
    }

    @Test
    public void whenCategoryWasDeletedThenCacheWasCleared() {
        this.cachedCategoriesRepository.delete(CATEGORY_ID);

        assertTrue(this.cache.isEmpty());
    }

    @Test
    public void whenCategoryWasStoredThenCacheWasCleared() {
        this.cachedCategoriesRepository.store(STORED);

        assertTrue(this.cache.isEmpty());
    }

    @Test
    public void whenCategoriesListWasCachedThenItsWillBeReturnedFromCache() {
        final Set<Category> categories = this.cachedCategoriesRepository.loadAll();

        assertEquals(1, categories.size());
        assertEquals(CACHED, categories.iterator().next());
    }

    @Test
    public void whenCategoriesListWasNotCachedThenItsWillBeReturnedFromDatastore() {
        this.cache.clear();

        final Set<Category> categories = this.cachedCategoriesRepository.loadAll();

        assertEquals(1, categories.size());
        assertEquals(STORED, categories.iterator().next());
    }

    @Test
    public void whenCategoriesListWasNotCachedThenCacheIsUpdatedWhileLoading() {
        this.cache.clear();

        this.cachedCategoriesRepository.loadAll();

        assertFalse(this.cache.isEmpty());
    }

    @Test
    public void whenRepositoryIsClearedThenCachedAndStoredItemsAreDeleted() {
        this.cachedCategoriesRepository.clear();

        assertTrue(this.cache.isEmpty());
        assertTrue(this.categoriesRepositoryStub.isEmpty());
    }

    @Test
    public void whenCategoryListWasCachedThenCategoryWillBeTookFromCache() {
        final Category category = this.cachedCategoriesRepository.load(CATEGORY_ID);

        assertEquals(CACHED, category);
    }

    @Test
    public void whenCategoryListWasNotCachedThenCategoryWillBeTookFromCacheAndCacheUpdated() {
        this.cache.clear();

        final Category category = this.cachedCategoriesRepository.load(CATEGORY_ID);

        assertEquals(STORED, category);
        assertFalse(this.cache.isEmpty());
    }

    @Test
    public void whenCategoryIsNotFoundThenNullWillBeReturned() {
        assertNull(this.cachedCategoriesRepository.load(UUID.randomUUID().toString()));
    }

}
