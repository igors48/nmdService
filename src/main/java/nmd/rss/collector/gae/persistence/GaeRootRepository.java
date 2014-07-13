package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.scheduler.cached.CachedFeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.cached.CachedFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.cached.CachedFeedHeadersRepository;
import nmd.rss.collector.updater.cached.CachedFeedItemsRepository;
import nmd.rss.reader.CachedCategoriesRepository;
import nmd.rss.reader.CachedReadFeedItemsRepository;
import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static nmd.rss.collector.gae.cache.MemCache.MEM_CACHE;
import static nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository.GAE_FEED_HEADERS_REPOSITORY;
import static nmd.rss.collector.gae.persistence.GaeFeedItemsRepository.GAE_FEED_ITEMS_REPOSITORY;
import static nmd.rss.collector.gae.persistence.GaeFeedUpdateTaskRepository.GAE_FEED_UPDATE_TASK_REPOSITORY;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.reader.gae.GaeCategoriesRepository.GAE_CATEGORIES_REPOSITORY;
import static nmd.rss.reader.gae.GaeReadFeedItemsRepository.GAE_READ_FEED_ITEMS_REPOSITORY;

/**
 * User: igu
 * Date: 15.10.13
 */
public class GaeRootRepository implements Transactions {

    private static final int MAX_CACHED_FEED_UPDATE_TASK_REPOSITORY_BEFORE_FLUSH = 300;

    public static final Transactions GAE_TRANSACTIONS = new GaeRootRepository();

    public static final FeedUpdateTaskSchedulerContextRepository GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY = new CachedFeedUpdateTaskSchedulerContextRepository(MEM_CACHE);
    public static final FeedItemsRepository GAE_CACHED_FEED_ITEMS_REPOSITORY = new CachedFeedItemsRepository(GAE_FEED_ITEMS_REPOSITORY, MEM_CACHE);
    public static final FeedUpdateTaskRepository GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY = new CachedFeedUpdateTaskRepository(GAE_FEED_UPDATE_TASK_REPOSITORY, MAX_CACHED_FEED_UPDATE_TASK_REPOSITORY_BEFORE_FLUSH, MEM_CACHE);
    public static final FeedHeadersRepository GAE_CACHED_FEED_HEADERS_REPOSITORY = new CachedFeedHeadersRepository(GAE_FEED_HEADERS_REPOSITORY, MEM_CACHE);
    public static final ReadFeedItemsRepository GAE_CACHED_READ_FEED_ITEMS_REPOSITORY = new CachedReadFeedItemsRepository(GAE_READ_FEED_ITEMS_REPOSITORY, MEM_CACHE);
    public static final CategoriesRepository GAE_CACHED_CATEGORIES_REPOSITORY = new CachedCategoriesRepository(GAE_CATEGORIES_REPOSITORY, MEM_CACHE);

    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";

    private static final Key ROOT_KEY = KeyFactory.createKey(FEEDS_ENTITY_KIND, FEEDS_ENTITY_KIND);

    private GaeRootRepository() {
        // empty
    }

    public static Key getEntityRootKey(final String uuid, final RootKind rootKind) {
        assertNotNull(uuid);
        assertNotNull(rootKind);

        return KeyFactory.createKey(ROOT_KEY, rootKind.value, uuid);
    }

    public static Entity loadEntity(final String uuid, final RootKind rootKind, final Kind kind, final boolean keysOnly) {
        assertNotNull(uuid);
        assertNotNull(kind);

        final PreparedQuery preparedQuery = prepareQuery(uuid, rootKind, kind, keysOnly);

        return preparedQuery.asSingleEntity();
    }

    public static List<Entity> loadEntities(final Kind kind) {
        assertNotNull(kind);

        final Query query = new Query(kind.value);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asList(withLimit(MAX_VALUE));
    }

    public static void deleteEntity(final String uuid, final RootKind rootKind, final Kind kind) {
        assertNotNull(uuid);
        assertNotNull(kind);

        final Entity victim = loadEntity(uuid, rootKind, kind, true);

        if (victim != null) {
            DATASTORE_SERVICE.delete(victim.getKey());
        }
    }

    private static PreparedQuery prepareQuery(final String uuid, final RootKind rootKind, final Kind kind, final boolean keysOnly) {
        final Key feedRootKey = getEntityRootKey(uuid, rootKind);
        final Query query = new Query(kind.value).setAncestor(feedRootKey);

        if (keysOnly) {
            query.setKeysOnly();
        }

        return DATASTORE_SERVICE.prepare(query);
    }

    @Override
    public Transaction beginOne() {
        return DATASTORE_SERVICE.beginTransaction();
    }

}
