package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * User: igu
 * Date: 15.10.13
 */
public class Entities {

    public static final String FEEDS_ENTITY_KIND = "ffeeds";
    public static final String FEED_ENTITY_KIND = "ffeed";
    public static final String HEADER_ENTITY_KIND = "hheader";

    public static Entity feeds() {
        return new Entity(FEEDS_ENTITY_KIND);
    }

    public static Entity feed(Key feedsKey) {
        return new Entity(FEED_ENTITY_KIND, feedsKey);
    }

    public static Entity header(String header, Key feedKey) {
        final Entity result = new Entity(HEADER_ENTITY_KIND, feedKey);

        result.setProperty("header", header);

        return result;
    }

    public Entities() {
        // empty
    }

}
