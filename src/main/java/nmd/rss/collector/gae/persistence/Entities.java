package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 15.10.13
 */
public class Entities {

    public static final String HEADER_ENTITY_KIND = "hheader";

    public static Entity header(String header, Key feedKey) {
        assertStringIsValid(header);
        assertNotNull(feedKey);

        final Entity result = new Entity(HEADER_ENTITY_KIND, feedKey);

        result.setProperty("header", header);

        return result;
    }

    public Entities() {
        // empty
    }

}
