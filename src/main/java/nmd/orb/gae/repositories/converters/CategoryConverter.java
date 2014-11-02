package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.reader.Category;

import static nmd.orb.gae.repositories.datastore.Kind.CATEGORY;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 20.03.2014
 */
public class CategoryConverter {

    private static final String ID = "id";
    private static final String NAME = "name";

    public static Entity convert(final Category category, final Key categoryKey) {
        assertNotNull(category);
        assertNotNull(categoryKey);

        final Entity entity = new Entity(CATEGORY.value, categoryKey);

        entity.setProperty(ID, category.uuid);
        entity.setProperty(NAME, category.name);

        return entity;
    }

    public static Category convert(final Entity entity) {
        assertNotNull(entity);

        final String id = (String) entity.getProperty(ID);
        final String name = (String) entity.getProperty(NAME);

        return new Category(id, name);
    }

}
