package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.orb.feed.FeedHeader;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.gae.repositories.datastore.RootKind;
import nmd.orb.repositories.FeedHeadersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static nmd.orb.gae.repositories.converters.FeedHeaderEntityConverter.FEED_LINK;
import static nmd.orb.gae.repositories.converters.FeedHeaderEntityConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.FEED_HEADER;
import static nmd.orb.gae.repositories.datastore.RootKind.FEED;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 15.10.13
 */
public enum GaeFeedHeadersRepository implements FeedHeadersRepository {

    INSTANCE;

    @Override
    public FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId.toString(), FEED, FEED_HEADER, false);

        return entity == null ? null : convert(entity);
    }

    @Override
    public List<FeedHeader> loadHeaders() {
        final List<Entity> entities = loadEntities(FEED_HEADER);

        final List<FeedHeader> headers = new ArrayList<>(entities.size());

        for (final Entity entity : entities) {
            final FeedHeader feedHeader = convert(entity);

            headers.add(feedHeader);
        }

        return headers;
    }

    @Override
    public void deleteHeader(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId.toString(), FEED, FEED_HEADER);
    }

    @Override
    public FeedHeader loadHeader(final String feedLink) {
        final Query query = new Query(FEED_HEADER.value)
                .setFilter(new Query.FilterPredicate(FEED_LINK, EQUAL, feedLink));
        final PreparedQuery preparedQuery = Datastore.INSTANCE.getDatastoreService().prepare(query);

        final Entity entity = preparedQuery.asSingleEntity();

        return entity == null ? null : convert(entity);
    }

    @Override
    public void storeHeader(final FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        final Entity entity = convert(feedHeader, getEntityRootKey(feedHeader.id.toString(), RootKind.FEED));

        Datastore.INSTANCE.getDatastoreService().put(entity);
    }

}
