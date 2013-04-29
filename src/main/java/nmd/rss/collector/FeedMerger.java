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
        List<FeedItem> retained = new ArrayList<>();
        List<FeedItem> added = new ArrayList<>();

        removePossibleDuplicates(old.items, young.items, removed, retained, added);
        checkMaximumCount(removed, retained, added, maximumCount);

        return new FeedMergeReport(removed, added);
    }

    private static void checkMaximumCount(final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added, final int maximumCount) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private static void removePossibleDuplicates(final List<FeedItem> olds, final List<FeedItem> youngs, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) {
        Map<String, FeedItem> oldItems = createUniqueFeedItemsLinkMap(olds);
        Map<String, FeedItem> youngItems = createUniqueFeedItemsLinkMap(youngs);

        for (String link : youngItems.keySet()) {
            FeedItem oldItem = oldItems.get(link);

            if (oldItem != null) {
                removed.add(oldItem);
                oldItems.remove(link);
            }

            added.add(youngItems.get(link));
        }

        retained.addAll(oldItems.values());
    }

    private static Map<String, FeedItem> createUniqueFeedItemsLinkMap(final List<FeedItem> feedItems) {
        Map<String, FeedItem> result = new HashMap<>();

        for (FeedItem current : feedItems) {
            result.put(current.link, current);
        }

        return result;
    }

    private FeedMerger() {
        // private
    }

}
