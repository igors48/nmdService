package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import nmd.rss.collector.feed.FeedItem;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class FeedItemConverter {

    public static final String KIND = "FeedItem";
    public static final String GUID = "guid";

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private static final String DATE = "date";

    public static Entity convert(final FeedItem item, final Key feedKey, final String feedId) {
        assertNotNull(item);
        assertNotNull(feedKey);

        final Entity entity = new Entity(createFeedEntityKind(feedId), feedKey);

        entity.setProperty(TITLE, item.title);
        entity.setProperty(DESCRIPTION, new Text(item.description));
        entity.setProperty(LINK, item.link);
        entity.setProperty(DATE, item.date);
        entity.setProperty(GUID, item.guid);

        return entity;
    }

    public static String createFeedEntityKind(String feedId) {
        return String.format("%s[%s]", KIND, feedId);
    }

    public static FeedItem convert(final Entity entity) {
        assertNotNull(entity);

        final String title = (String) entity.getProperty(TITLE);
        final String description = ((Text) entity.getProperty(DESCRIPTION)).getValue();
        final String link = (String) entity.getProperty(LINK);
        final Date date = (Date) entity.getProperty(DATE);
        final String guid = (String) entity.getProperty(GUID);

        return new FeedItem(title, description, link, date, guid);
    }

}
