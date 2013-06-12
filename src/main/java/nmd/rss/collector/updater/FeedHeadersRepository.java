package nmd.rss.collector.updater;

import nmd.rss.collector.Repository;
import nmd.rss.collector.feed.FeedHeader;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public interface FeedHeadersRepository extends Repository {

    FeedHeader loadHeader(UUID feedId);

    void deleteHeader(UUID feedId);

    FeedHeader loadHeader(String feedLink);

    void storeHeader(FeedHeader feedHeader);

}
