package nmd.orb.repositories;

import nmd.orb.reader.ReadFeedItems;

import java.util.List;
import java.util.UUID;

/**
 * User: igu
 * Date: 22.10.13
 */
public interface ReadFeedItemsRepository {

    List<ReadFeedItems> loadAll();

    ReadFeedItems load(UUID feedId);

    void store(ReadFeedItems readFeedItems);

    void delete(UUID feedId);

}
