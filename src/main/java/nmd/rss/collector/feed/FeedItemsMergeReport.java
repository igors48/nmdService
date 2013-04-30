package nmd.rss.collector.feed;

import java.util.Collections;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public class FeedItemsMergeReport {

    public final List<FeedItem> removed;
    public final List<FeedItem> retained;
    public final List<FeedItem> added;

    public FeedItemsMergeReport(final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) {
        assertNotNull(removed);
        this.removed = Collections.unmodifiableList(removed);

        assertNotNull(retained);
        this.retained = Collections.unmodifiableList(retained);

        assertNotNull(added);
        this.added = Collections.unmodifiableList(added);
    }

}
