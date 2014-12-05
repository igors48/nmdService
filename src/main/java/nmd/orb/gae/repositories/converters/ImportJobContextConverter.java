package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.services.importer.ImportJobContext;

import static nmd.orb.gae.repositories.datastore.Kind.IMPORT;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * @author : igu
 */
public class ImportJobContextConverter {

    public static Entity convert(final ImportJobContext context, final Key key) {
        assertNotNull(context);
        assertNotNull(key);

        final Entity entity = new Entity(IMPORT.value, key);

        return entity;
    }

    public static ImportJobContext convert(final Entity entity) {
        assertNotNull(entity);

        return null;
    }

}
