package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.rss.reader.ReadFeedItems;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.11.13
 */
public class ReadFeedItemsConverter {

    public static final String KIND = "ReadFeedItem";

    private static final Gson GSON = new Gson();

    private static final String FEED_ID = "feedId";
    private static final String COUNT = "count";
    private static final String READ_ITEMS = "readItems";
    private static final String LAST_UPDATE = "lastUpdate";

    private static final Type READ_FEED_ITEM_HELPER_SET_TYPE = new TypeToken<Set<String>>() {
    }.getType();

    public static Entity convert(final Key feedKey, final UUID feedId, final ReadFeedItems readFeedItems) {
        assertNotNull(feedKey);
        assertNotNull(feedId);
        assertNotNull(readFeedItems);

        final Entity entity = new Entity(KIND, feedKey);

        entity.setProperty(FEED_ID, feedId.toString());
        entity.setProperty(COUNT, readFeedItems.itemIds.size());
        entity.setProperty(LAST_UPDATE, readFeedItems.lastUpdate);

        final String readFeedItemsAsString = GSON.toJson(readFeedItems.itemIds, READ_FEED_ITEM_HELPER_SET_TYPE);

        entity.setProperty(READ_ITEMS, new Text(readFeedItemsAsString));

        return entity;
    }

    public static ReadFeedItems convert(Entity entity) {
        assertNotNull(entity);

        //TODO remove hasProperty check after full table update
        final Date lastUpdate = entity.hasProperty(LAST_UPDATE) ? (Date) entity.getProperty(LAST_UPDATE) : new Date();
        final String readFeedItemsAsString = ((Text) entity.getProperty(READ_ITEMS)).getValue();
        final Set<String> readItemsIds = GSON.fromJson(readFeedItemsAsString, READ_FEED_ITEM_HELPER_SET_TYPE);

        return new ReadFeedItems(lastUpdate, readItemsIds);
    }

}
