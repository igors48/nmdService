package nmd.rss.collector;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public class FeedMergeReport {

    private final List<FeedItem> removed;
    private final List<FeedItem> added;

    public FeedMergeReport(final List<FeedItem> removed, final List<FeedItem> added) {
        assertNotNull(removed);
        this.removed = removed;

        assertNotNull(added);
        this.added = added;
    }

    public List<FeedItem> getRemoved() {
        return this.removed;
    }

    public List<FeedItem> getAdded() {
        return this.added;
    }

}
