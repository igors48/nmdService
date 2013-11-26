package nmd.rss.reader;

import java.util.Set;
import java.util.UUID;

/**
 * User: igu
 * Date: 22.10.13
 */
public interface ReadFeedItemsRepository {

    Set<String> load(UUID feedId);

    void store(UUID feedId, String itemId);

    void delete(UUID feedId);

}
