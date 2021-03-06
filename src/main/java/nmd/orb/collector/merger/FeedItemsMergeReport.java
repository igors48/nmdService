package nmd.orb.collector.merger;

import nmd.orb.feed.FeedItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nmd.orb.util.Assert.assertNotNull;

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

    public List<FeedItem> getAddedAndRetained() {
        final List<FeedItem> result = new ArrayList<>();

        result.addAll(this.added);
        result.addAll(this.retained);

        return result;
    }
}
