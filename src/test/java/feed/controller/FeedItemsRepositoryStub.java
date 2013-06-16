package feed.controller;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public void updateItems(final UUID feedId, final List<FeedItem> feedItems) {
        this.items.put(feedId, feedItems);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        return this.items.get(feedId);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        this.items.remove(feedId);
    }

    @Override
    public List loadAllEntities() {
        return null;
    }

    @Override
    public void remove(final Object victim) {

    }

}
