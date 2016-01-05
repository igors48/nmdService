package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static java.lang.Integer.MAX_VALUE;
import static nmd.orb.gae.repositories.converters.ReadFeedItemsConverter.CATEGORY_ID;
import static nmd.orb.gae.repositories.converters.ReadFeedItemsConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.READ_FEED_ITEM;
import static nmd.orb.gae.repositories.datastore.RootKind.FEED;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.reader.ReadFeedItems.empty;
import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.Assert.guard;

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
    public List<ReadFeedItems> load(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        final List<ReadFeedItems> result = new ArrayList<>();

        final Query query = new Query(READ_FEED_ITEM.value)
                .setFilter(new Query.FilterPredicate(CATEGORY_ID, EQUAL, categoryId));
        final PreparedQuery preparedQuery = Datastore.INSTANCE.getDatastoreService().prepare(query);

        final List<Entity> entities = preparedQuery.asList(withLimit(MAX_VALUE));

        for (final Entity entity : entities) {
            final ReadFeedItems current = convert(entity);
            result.add(current);
        }

        return result;
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
