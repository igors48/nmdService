package unit.feed.controller;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
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
    public void mergeItems(final UUID feedId, final FeedItemsMergeReport feedItemsMergeReport) {
        final List<FeedItem> feedItems = new ArrayList<>();

        feedItems.addAll(feedItemsMergeReport.retained);
        feedItems.addAll(feedItemsMergeReport.added);

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

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

}
