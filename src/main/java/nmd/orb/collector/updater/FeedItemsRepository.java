package nmd.orb.collector.updater;

import nmd.orb.feed.FeedItem;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public interface FeedItemsRepository {

    void storeItems(UUID feedId, List<FeedItem> items);

    List<FeedItem> loadItems(UUID feedId);

    void deleteItems(UUID feedId);

}
