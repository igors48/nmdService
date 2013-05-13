package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.feed.FeedItemEntity;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public interface FeedItemsRepository {
    /*
    int removeItem(UUID feedItemId);

    void addItem(UUID feedId, FeedItem feedItem);
    */
    void storeItems(UUID feedId, List<FeedItem> feedItems);

    List<FeedItem> loadItems(UUID feedId);

    List<FeedItemEntity> loadAllItemsKeys();

    void removeEntity(FeedItemEntity victim);

}
