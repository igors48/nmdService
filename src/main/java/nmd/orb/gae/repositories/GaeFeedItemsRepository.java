package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.feed.FeedItem;
import nmd.orb.feed.FeedItemShortcut;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.repositories.FeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.feed.FeedItem.createShortcuts;
import static nmd.orb.gae.repositories.converters.FeedItemListEntityConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.FEED_ITEM;
import static nmd.orb.gae.repositories.datastore.RootKind.FEED;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 16.10.13
 */
public enum GaeFeedItemsRepository implements FeedItemsRepository {

    INSTANCE;

    @Override
    public void storeItems(final UUID feedId, final List<FeedItem> items) {
        assertNotNull(feedId);
        assertNotNull(items);

        deleteItems(feedId);

        final Key feedRootKey = getEntityRootKey(feedId.toString(), FEED);
        final Entity entity = convert(feedRootKey, feedId, items);

        Datastore.INSTANCE.getDatastoreService().put(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId.toString(), FEED, FEED_ITEM, false);

        return entity == null ? new ArrayList<FeedItem>() : convert(entity);
    }

    @Override
    public List<FeedItemShortcut> loadItemsShortcuts(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> items = this.loadItems(feedId);

        return createShortcuts(items);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId.toString(), FEED, FEED_ITEM);
    }

}
