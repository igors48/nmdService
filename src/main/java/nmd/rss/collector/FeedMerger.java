package nmd.rss.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public final class FeedMerger {

    public static FeedMergeReport merge(final Feed old, final Feed young, final int maximumCount) {
        assertNotNull(old);
        assertNotNull(young);
        assertPositive(maximumCount);

        List<FeedItem> removed = new ArrayList<>();
        List<FeedItem> added = new ArrayList<>();

        Map<String, FeedItem> oldItems = createUniqueFeedItemsLinkMap(old);
        Map<String, FeedItem> youngItems = createUniqueFeedItemsLinkMap(young);

        return createFeedMergeReport(removed, added, oldItems, youngItems);
    }

    private static FeedMergeReport createFeedMergeReport(final List<FeedItem> removed, final List<FeedItem> added, final Map<String, FeedItem> oldItems, final Map<String, FeedItem> youngItems) {

        for (String link : youngItems.keySet()) {
            FeedItem oldItem = oldItems.get(link);

            if (oldItem != null) {
                removed.add(oldItem);
                oldItems.remove(link);
            }

            added.add(youngItems.get(link));
        }

        return new FeedMergeReport(removed, added);
    }

    private static Map<String, FeedItem> createUniqueFeedItemsLinkMap(final Feed feed) {
        Map<String, FeedItem> result = new HashMap<>();

        for (FeedItem current : feed.items) {
            result.put(current.link, current);
        }

        return result;
    }

    private FeedMerger() {
        // private
    }

}
