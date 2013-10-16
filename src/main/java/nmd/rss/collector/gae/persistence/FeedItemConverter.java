package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.feed.FeedItem;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class FeedItemConverter {

    public static final String KIND = "FeedItem";

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private static final String DATE = "date";
    private static final String GUID = "guid";

    public static Entity convert(FeedItem item, Key feedKey) {
        assertNotNull(item);
        assertNotNull(feedKey);

        final Entity entity = new Entity(KIND, feedKey);

        entity.setProperty(TITLE, item.title);
        entity.setProperty(DESCRIPTION, item.description);
        entity.setProperty(LINK, item.link);
        entity.setProperty(DATE, item.date);
        entity.setProperty(GUID, item.guid);

        return entity;
    }

    public static FeedItem convert(Entity entity) {
        assertNotNull(entity);

        final String title = (String) entity.getProperty(TITLE);
        final String description = (String) entity.getProperty(DESCRIPTION);
        final String link = (String) entity.getProperty(LINK);
        final Date date = (Date) entity.getProperty(DATE);
        final String guid = (String) entity.getProperty(GUID);

        return new FeedItem(title, description, link, date, guid);
    }

}
