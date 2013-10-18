package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.05.13
 */
public interface FeedItemsRepository {

    void mergeItems(UUID feedId, FeedItemsMergeReport feedItemsMergeReport);

    List<FeedItem> loadItems(UUID feedId);

    void deleteItems(UUID feedId);

}
