package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.gae.persistence.RootKind;
import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.gae.persistence.Kind.CATEGORY;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.reader.gae.CategoryConverter.convert;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class GaeCategoriesRepository implements CategoriesRepository {

    public static final CategoriesRepository GAE_CATEGORIES_REPOSITORY = new GaeCategoriesRepository();

    @Override
    public void store(final Category category) {
        assertNotNull(category);

        final Key rootKey = getEntityRootKey(category.uuid, RootKind.CATEGORY);
        final Entity entity = convert(category, rootKey);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public Category load(final String categoryId) {
        assertStringIsValid(categoryId);

        final Entity entity = loadEntity(categoryId, RootKind.CATEGORY, CATEGORY, false);

        return entity == null ? null : convert(entity);
    }

    @Override
    public Set<Category> loadAll() {
        final Set<Category> categories = new HashSet<>();

        final List<Entity> entities = loadEntities(CATEGORY);

        for (final Entity entity : entities) {
            final Category category = convert(entity);

            categories.add(category);
        }

        return categories;
    }

    @Override
    public void delete(final String categoryId) {
        assertStringIsValid(categoryId);

        deleteEntity(categoryId, RootKind.CATEGORY, CATEGORY);
    }

    @Override
    public void clear() {
        final List<Entity> entities = loadEntities(CATEGORY);

        for (final Entity entity : entities) {
            DATASTORE_SERVICE.delete(entity.getKey());
        }
    }

}
