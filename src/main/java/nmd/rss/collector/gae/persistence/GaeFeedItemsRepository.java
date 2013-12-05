package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.FeedItemListEntityConverter.KIND;
import static nmd.rss.collector.gae.persistence.FeedItemListEntityConverter.convert;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
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
        final Entity entity = convert(feedRootKey, feedId, storedItems);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId, KIND, false);

        return entity == null ? new ArrayList<FeedItem>() : convert(entity);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId, KIND);
    }

}
