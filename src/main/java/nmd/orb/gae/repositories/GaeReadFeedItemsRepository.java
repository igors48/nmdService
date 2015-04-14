package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.gae.repositories.converters.ReadFeedItemsConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.READ_FEED_ITEM;
import static nmd.orb.gae.repositories.datastore.RootKind.FEED;
import static nmd.orb.reader.ReadFeedItems.empty;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 26.11.13
 */
public enum GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    INSTANCE;

    @Override
    public List<ReadFeedItems> loadAll() {
        final List<ReadFeedItems> list = new ArrayList<>();

        final List<Entity> entities = loadEntities(READ_FEED_ITEM);

        for (final Entity entity : entities) {
            final ReadFeedItems readFeedItems = convert(entity);

            list.add(readFeedItems);
        }

        return list;
    }

    @Override
    public ReadFeedItems load(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId.toString(), FEED, READ_FEED_ITEM, false);

        return entity == null ? empty(feedId) : convert(entity);
    }

    @Override
    public void store(final ReadFeedItems readFeedItems) {
        assertNotNull(readFeedItems);

        delete(readFeedItems.feedId);

        final Key feedRootKey = getEntityRootKey(readFeedItems.feedId.toString(), FEED);
        final Entity entity = convert(feedRootKey, readFeedItems);

        Datastore.INSTANCE.getDatastoreService().put(entity);
    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId.toString(), FEED, READ_FEED_ITEM);
    }

}
