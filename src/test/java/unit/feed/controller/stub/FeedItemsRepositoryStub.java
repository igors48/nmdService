package unit.feed.controller.stub;

import nmd.orb.feed.FeedItem;
import nmd.orb.repositories.FeedItemsRepository;

import java.util.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.06.13
 */
public class FeedItemsRepositoryStub implements FeedItemsRepository {

    private final Map<UUID, List<FeedItem>> items;

    private int storeCount;

    public FeedItemsRepositoryStub() {
        this.items = new LinkedHashMap<>();
        this.storeCount = 0;
    }

    @Override
    public void storeItems(UUID feedId, List<FeedItem> items) {
        this.items.put(feedId, items);
        ++this.storeCount;
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

    public int getStoreCount() {
        return this.storeCount;
    }

}
