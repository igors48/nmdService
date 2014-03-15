package unit.feed.controller;

import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.*;

import static nmd.rss.reader.ReadFeedItems.empty;

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
    public List<ReadFeedItems> loadAll() {
        return new ArrayList<>(this.readFeeds.values());
    }

    @Override
    public ReadFeedItems load(final UUID feedId) {
        final ReadFeedItems result = this.readFeeds.get(feedId);

        return (result == null) ? empty(feedId) : result;
    }

    @Override
    public void store(final ReadFeedItems readFeedItems) {
        this.readFeeds.put(readFeedItems.feedId, readFeedItems);
    }

    @Override
    public void delete(final UUID feedId) {
        this.readFeeds.remove(feedId);
    }

    public boolean isEmpty() {
        return this.readFeeds.isEmpty();
    }

}
