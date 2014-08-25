package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.gae.repositories.datastore.RootKind;
import nmd.orb.reader.Category;
import nmd.orb.repositories.CategoriesRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nmd.orb.gae.repositories.converters.CategoryConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.CATEGORY;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.03.14
 */
public class GaeCategoriesRepository implements CategoriesRepository {

    public static final CategoriesRepository GAE_CATEGORIES_REPOSITORY = new GaeCategoriesRepository();

    @Override
    public void store(final Category category) {
        guard(notNull(category));

        final Key rootKey = getEntityRootKey(category.uuid, RootKind.CATEGORY);
        final Entity entity = convert(category, rootKey);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public Category load(final String categoryId) {
        guard(isValidCategoryId(categoryId));

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
        guard(isValidCategoryId(categoryId));

        deleteEntity(categoryId, RootKind.CATEGORY, CATEGORY);
    }

    @Override
    public void clear() {
        final List<Entity> entities = loadEntities(CATEGORY);

        for (final Entity entity : entities) {
            DATASTORE_SERVICE.delete(entity.getKey());
        }
    }

    private GaeCategoriesRepository() {
        // empty
    }

}
