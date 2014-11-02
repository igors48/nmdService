package unit.feed.controller.stub;

import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.ReadFeedItemsRepository;

import java.util.*;

import static nmd.orb.reader.ReadFeedItems.empty;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
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
