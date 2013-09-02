package nmd.rss.reader;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.09.13
 */
public class ReadFeedItems {

    public final UUID feedId;
    public final Set<String> readItemsGuids;

    public ReadFeedItems(final UUID feedId) {
        assertNotNull(feedId);
        this.feedId = feedId;

        this.readItemsGuids = new HashSet<>();
    }

    public void addReadItemGuid(final String feedItemGuid) {
        assertStringIsValid(feedItemGuid);
        this.readItemsGuids.add(feedItemGuid);
    }

    public Set<String> getReadItemsGuids() {
        return this.readItemsGuids;
    }

}
