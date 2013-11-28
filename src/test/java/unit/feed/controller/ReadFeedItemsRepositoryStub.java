package unit.feed.controller;

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
        final Set<String> uuids = this.readFeeds.get(feedId);

        return uuids == null ? new HashSet<String>() : uuids;
    }

    @Override
    public void store(final UUID feedId, final Set<String> itemIds) {
        this.readFeeds.put(feedId, itemIds);
    }

    @Override
    public void delete(final UUID feedId) {
        this.readFeeds.remove(feedId);
    }

}
