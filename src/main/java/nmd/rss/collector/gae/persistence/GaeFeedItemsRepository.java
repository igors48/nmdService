package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.DATASTORE_SERVICE;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.getFeedRootKey;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class GaeFeedItemsRepository implements FeedItemsRepository {

    @Override
    public void mergeItems(final UUID feedId, final FeedItemsMergeReport feedItemsMergeReport) {
        assertNotNull(feedId);
        assertNotNull(feedItemsMergeReport);

        final boolean nothingChanged = feedItemsMergeReport.added.isEmpty() && feedItemsMergeReport.removed.isEmpty();

        if (nothingChanged) {
            return;
        }

        deleteItems(feedId);

        final List<FeedItem> storedItems = new ArrayList<>();
        storedItems.addAll(feedItemsMergeReport.retained);
        storedItems.addAll(feedItemsMergeReport.added);

        final Key feedRootKey = getFeedRootKey(feedId);
        final Entity entity = FeedItemListEntityConverter.convert(feedRootKey, feedId, storedItems);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final Key feedRootKey = getFeedRootKey(feedId);

        //TODO does this check really need
        if (feedRootKey == null) {
            return null;
        }

        final Query query = new Query(FeedItemListEntityConverter.KIND).setAncestor(feedRootKey);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final Entity entity = preparedQuery.asSingleEntity();

        return entity == null ? new ArrayList<FeedItem>() : FeedItemListEntityConverter.convert(entity);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        final Key feedRootKey = getFeedRootKey(feedId);
        final Query query = new Query(FeedItemListEntityConverter.KIND).setAncestor(feedRootKey).setKeysOnly();
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final Entity victim = preparedQuery.asSingleEntity();

        if (victim != null) {
            DATASTORE_SERVICE.delete(victim.getKey());
        }
    }

}
