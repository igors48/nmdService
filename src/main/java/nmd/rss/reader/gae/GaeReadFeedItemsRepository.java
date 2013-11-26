package nmd.rss.reader.gae;

import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 26.11.13
 */
public class GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    @Override
    public Set<String> load(UUID feedId) {
        assertNotNull(feedId);

        return null;
    }

    @Override
    public void store(UUID feedId, String itemId) {
        assertNotNull(feedId);
        assertStringIsValid(itemId);

    }

    @Override
    public void delete(UUID feedId) {
        assertNotNull(feedId);

    }

}
