package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.reader.Category;

import static nmd.rss.collector.gae.persistence.Kind.CATEGORY;
import static nmd.rss.collector.util.Assert.assertNotNull;

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
