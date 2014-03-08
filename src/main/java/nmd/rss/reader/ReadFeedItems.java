package nmd.rss.reader;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.reader.Category.DEFAULT_CATEGORY_ID;

/**
 * User: igu
 * Date: 25.12.13
 */
public class ReadFeedItems implements Serializable {

    public static final ReadFeedItems EMPTY = new ReadFeedItems(new Date(), new HashSet<String>(), new HashSet<String>(), DEFAULT_CATEGORY_ID);

    public final Date lastUpdate;
    public final Set<String> readItemIds;
    public final Set<String> readLaterItemIds;
    public final String categoryId;

    public ReadFeedItems(final Date lastUpdate, final Set<String> readItemIds, final Set<String> readLaterItemIds, final String categoryId) {
        assertNotNull(lastUpdate);
        this.lastUpdate = lastUpdate;

        assertNotNull(readItemIds);
        this.readItemIds = readItemIds;

        assertNotNull(readLaterItemIds);
        this.readLaterItemIds = readLaterItemIds;

        assertStringIsValid(categoryId);
        this.categoryId = categoryId;
    }

}
