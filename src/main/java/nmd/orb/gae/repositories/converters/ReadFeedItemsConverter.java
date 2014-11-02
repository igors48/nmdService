package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.orb.reader.ReadFeedItems;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nmd.orb.gae.repositories.datastore.Kind.READ_FEED_ITEM;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.11.13
 */
public class ReadFeedItemsConverter {

    private static final Gson GSON = new Gson();

    private static final String FEED_ID = "feedId";
    private static final String COUNT = "count";
    private static final String READ_ITEMS = "readItems";
    private static final String READ_LATER_ITEMS = "readLaterItems";
    private static final String LAST_UPDATE = "lastUpdate";
    private static final String CATEGORY_ID = "categoryId";

    private static final Type SET_HELPER_TYPE = new TypeToken<Set<String>>() {
    }.getType();

    public static Entity convert(final Key feedKey, final ReadFeedItems readFeedItems) {
        assertNotNull(feedKey);
        assertNotNull(readFeedItems);

        final Entity entity = new Entity(READ_FEED_ITEM.value, feedKey);

        entity.setProperty(FEED_ID, readFeedItems.feedId.toString());
        entity.setProperty(COUNT, readFeedItems.readItemIds.size());
        entity.setProperty(LAST_UPDATE, readFeedItems.lastUpdate);
        entity.setProperty(CATEGORY_ID, readFeedItems.categoryId);

        final String readFeedItemsAsString = GSON.toJson(readFeedItems.readItemIds, SET_HELPER_TYPE);

        entity.setProperty(READ_ITEMS, new Text(readFeedItemsAsString));

        final String readLaterFeedItemsAsString = GSON.toJson(readFeedItems.readLaterItemIds, SET_HELPER_TYPE);

        entity.setProperty(READ_LATER_ITEMS, new Text(readLaterFeedItemsAsString));

        return entity;
    }

    public static ReadFeedItems convert(Entity entity) {
        assertNotNull(entity);

        final Date lastUpdate = (Date) entity.getProperty(LAST_UPDATE);

        final String readFeedItemsAsString = ((Text) entity.getProperty(READ_ITEMS)).getValue();
        final Set<String> readItemsIds = GSON.fromJson(readFeedItemsAsString, SET_HELPER_TYPE);

        final String readLaterFeedItemsAsString = ((Text) entity.getProperty(READ_LATER_ITEMS)).getValue();

        final Set<String> readLaterItemIds;

        if (readLaterFeedItemsAsString.isEmpty()) {
            readLaterItemIds = new HashSet<>();
        } else {
            readLaterItemIds = GSON.fromJson(readLaterFeedItemsAsString, SET_HELPER_TYPE);
        }

        final String categoryId = (String) entity.getProperty(CATEGORY_ID);
        final UUID feedId = UUID.fromString((String) entity.getProperty(FEED_ID));

        return new ReadFeedItems(feedId, lastUpdate, readItemsIds, readLaterItemIds, categoryId);
    }

}
