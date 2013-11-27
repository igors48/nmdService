package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.11.13
 */
public class ReadFeedIdSetConverter {

    public static final String KIND = "ReadFeedItem";

    private static final Gson GSON = new Gson();

    private static final String FEED_ID = "feedId";
    private static final String COUNT = "count";
    private static final String READ_ITEMS = "readItems";

    private static final Type READ_FEED_ITEM_HELPER_SET_TYPE = new TypeToken<Set<String>>() {
    }.getType();

    public static Entity convert(final Key feedKey, final UUID feedId, final Set<String> readFeedItems) {
        assertNotNull(feedKey);
        assertNotNull(feedId);
        assertNotNull(readFeedItems);

        final Entity entity = new Entity(KIND, feedKey);

        entity.setProperty(FEED_ID, feedId.toString());
        entity.setProperty(COUNT, readFeedItems.size());

        final String readFeedItemsAsString = GSON.toJson(readFeedItems, READ_FEED_ITEM_HELPER_SET_TYPE);

        entity.setProperty(READ_ITEMS, new Text(readFeedItemsAsString));

        return entity;
    }

    public static Set<String> convert(Entity entity) {
        assertNotNull(entity);

        final String readFeedItemsAsString = ((Text) entity.getProperty(READ_ITEMS)).getValue();

        return GSON.fromJson(readFeedItemsAsString, READ_FEED_ITEM_HELPER_SET_TYPE);
    }

}
