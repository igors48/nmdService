package unit.feed.controller.stub;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.06.13
 */
public class FeedItemsRepositoryStub implements FeedItemsRepository {

    private final Map<UUID, List<FeedItem>> items;

    public FeedItemsRepositoryStub() {
        this.items = new HashMap<>();
    }

    @Override
    public void storeItems(UUID feedId, List<FeedItem> items) {
        this.items.put(feedId, items);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        final List<FeedItem> feedItems = this.items.get(feedId);

        return feedItems == null ? null : new ArrayList<>(feedItems);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        this.items.remove(feedId);
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

}
