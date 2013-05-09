package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedHeader;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public interface FeedHeadersRepository {

    FeedHeader loadHeader(UUID feedId);

}
