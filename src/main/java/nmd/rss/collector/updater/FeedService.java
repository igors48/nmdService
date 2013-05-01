package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public interface FeedService {

    FeedHeader loadHeader(UUID feedId) throws FeedServiceException;

    List<FeedItem> loadItems(UUID feedId) throws FeedServiceException;

    void updateItems(UUID feedId, List<FeedItem> removed, List<FeedItem> added);
}
