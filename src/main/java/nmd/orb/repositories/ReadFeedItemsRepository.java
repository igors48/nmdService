package nmd.orb.repositories;

import nmd.orb.reader.ReadFeedItems;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 22.10.13
 */
public interface ReadFeedItemsRepository {

    List<ReadFeedItems> loadAll();

    List<ReadFeedItems> load(String categoryId);

    ReadFeedItems load(UUID feedId);

    void store(ReadFeedItems readFeedItems);

    void delete(UUID feedId);

}
