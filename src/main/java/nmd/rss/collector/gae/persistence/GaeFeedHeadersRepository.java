package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.updater.FeedHeadersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static nmd.rss.collector.gae.persistence.FeedHeaderEntityConverter.FEED_LINK;
import static nmd.rss.collector.gae.persistence.FeedHeaderEntityConverter.convert;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.gae.persistence.Kind.FEED_HEADER;
import static nmd.rss.collector.gae.persistence.RootKind.FEED;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 15.10.13
 */
public class GaeFeedHeadersRepository implements FeedHeadersRepository {

    public static final FeedHeadersRepository GAE_FEED_HEADERS_REPOSITORY = new GaeFeedHeadersRepository();

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
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final Entity entity = preparedQuery.asSingleEntity();

        return entity == null ? null : convert(entity);
    }

    @Override
    public void storeHeader(final FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        final Entity entity = convert(feedHeader, getEntityRootKey(feedHeader.id.toString(), RootKind.FEED));

        DATASTORE_SERVICE.put(entity);
    }

    private GaeFeedHeadersRepository() {
        // empty
    }

}
