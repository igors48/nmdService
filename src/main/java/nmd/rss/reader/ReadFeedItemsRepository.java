package nmd.rss.reader;

import java.util.UUID;

/**
 * User: igu
 * Date: 22.10.13
 */
public interface ReadFeedItemsRepository {

    ReadFeedItems load(UUID feedId);

    void store(UUID feedId, ReadFeedItems readFeedItems);

}
