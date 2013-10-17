package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.updater.FeedHeadersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static java.lang.Integer.MAX_VALUE;
import static nmd.rss.collector.gae.persistence.FeedHeaderConverter.FEED_LINK;
import static nmd.rss.collector.gae.persistence.FeedHeaderConverter.KIND;
import static nmd.rss.collector.gae.persistence.RootRepository.DATASTORE_SERVICE;
import static nmd.rss.collector.gae.persistence.RootRepository.getFeedRootKey;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 15.10.13
 */
public class NewFeedHeadersRepository implements FeedHeadersRepository {

    @Override
    public FeedHeader loadHeader(UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        return FeedHeaderConverter.convert(entity);
    }

    @Override
    public List<FeedHeader> loadHeaders() {
        final Query query = new Query(KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final List<Entity> entities = preparedQuery.asList(withLimit(MAX_VALUE));

        final List<FeedHeader> headers = new ArrayList<>(entities.size());

        for (final Entity entity : entities) {
            final FeedHeader feedHeader = FeedHeaderConverter.convert(entity);

            headers.add(feedHeader);
        }

        return headers;
    }

    @Override
    public void deleteHeader(UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        DATASTORE_SERVICE.delete(entity.getKey());
    }

    @Override
    public FeedHeader loadHeader(String feedLink) {
        final Query query = new Query(KIND)
                .setFilter(new Query.FilterPredicate(FEED_LINK, EQUAL, feedLink));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final Entity entity = preparedQuery.asSingleEntity();

        return entity == null ? null : FeedHeaderConverter.convert(entity);
    }

    @Override
    public void storeHeader(FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        final Entity entity = FeedHeaderConverter.convert(feedHeader, getFeedRootKey(feedHeader.id));

        DATASTORE_SERVICE.put(entity);
    }

    private Entity getEntity(UUID feedId) {
        final Query query = new Query(KIND).setAncestor(getFeedRootKey(feedId));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
    }

}
