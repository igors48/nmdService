package feed.controller;

import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.*;

/**
 * User: igu
 * Date: 22.10.13
 */
public class ReadFeedItemsRepositoryStub implements ReadFeedItemsRepository {

    private final Map<UUID, Set<String>> readFeeds;

    public ReadFeedItemsRepositoryStub() {
        this.readFeeds = new HashMap<>();
    }

    @Override
    public Set<String> load(final UUID feedId) {
        final Set<String> strings = this.readFeeds.get(feedId);

        return strings == null ? new HashSet<String>() : strings;
    }

    @Override
    public void store(final UUID feedId, final Set<String> readFeedItems) {
        this.readFeeds.put(feedId, readFeedItems);
    }

    @Override
    public void delete(final UUID feedId) {
        this.readFeeds.remove(feedId);
    }

}
