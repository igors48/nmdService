package nmd.rss.reader;

import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.09.13
 */
public class ReadFeedItems {

    public final Set<String> readItemsGuids;

    public ReadFeedItems(final Set<String> readItemsGuids) {
        assertNotNull(readItemsGuids);
        this.readItemsGuids = readItemsGuids;
    }

}
