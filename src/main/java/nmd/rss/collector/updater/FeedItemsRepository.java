package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedItem;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public interface FeedItemsRepository {

    void updateItems(UUID feedId, List<FeedItem> feedItems);

    List<FeedItem> loadItems(UUID feedId);

    List loadAllEntities();

    void removeEntity(Object victim);

}