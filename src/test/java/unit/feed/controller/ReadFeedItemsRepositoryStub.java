package unit.feed.controller;

import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.reader.ReadFeedItems.EMPTY;

/**
 * User: igu
 * Date: 22.10.13
 */
public class ReadFeedItemsRepositoryStub implements ReadFeedItemsRepository {

    private final Map<UUID, ReadFeedItems> readFeeds;

    public ReadFeedItemsRepositoryStub() {
        this.readFeeds = new HashMap<>();
    }

    @Override
    public ReadFeedItems load(final UUID feedId) {
        final ReadFeedItems result = this.readFeeds.get(feedId);

        return result == null ? EMPTY : result;
    }

    @Override
    public void store(final UUID feedId, final ReadFeedItems readFeedItems) {
        this.readFeeds.put(feedId, readFeedItems);
    }

    @Override
    public void delete(final UUID feedId) {
        this.readFeeds.remove(feedId);
    }

    public boolean isEmpty() {
        return this.readFeeds.isEmpty();
    }

}
