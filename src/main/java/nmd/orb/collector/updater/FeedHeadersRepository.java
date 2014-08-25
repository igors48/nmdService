package nmd.orb.collector.updater;

import nmd.orb.collector.feed.FeedHeader;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public interface FeedHeadersRepository {

    FeedHeader loadHeader(UUID feedId);

    List<FeedHeader> loadHeaders();

    void deleteHeader(UUID feedId);

    FeedHeader loadHeader(String feedLink);

    void storeHeader(FeedHeader feedHeader);

}
