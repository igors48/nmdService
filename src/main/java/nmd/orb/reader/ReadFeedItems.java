package nmd.orb.reader;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nmd.orb.reader.Category.MAIN_CATEGORY_ID;
import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 25.12.13
 */
public class ReadFeedItems implements Serializable {

    public final UUID feedId;
    public final Date lastUpdate;
    public final Set<String> readItemIds;
    public final Set<String> readLaterItemIds;
    public final String categoryId;

    public ReadFeedItems(final UUID feedId, final Date lastUpdate, final Set<String> readItemIds, final Set<String> readLaterItemIds, final String categoryId) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertNotNull(lastUpdate);
        this.lastUpdate = lastUpdate;

        assertNotNull(readItemIds);
        this.readItemIds = readItemIds;

        assertNotNull(readLaterItemIds);
        this.readLaterItemIds = readLaterItemIds;

        assertStringIsValid(categoryId);
        this.categoryId = categoryId;
    }

    public static ReadFeedItems empty(final UUID feedId) {
        assertNotNull(feedId);

        return new ReadFeedItems(feedId, new Date(0), new HashSet<String>(), new HashSet<String>(), MAIN_CATEGORY_ID);
    }

    public ReadFeedItems changeCategory(final String categoryId) {
        assertStringIsValid(categoryId);

        final Set<String> readGuids = new HashSet<>();
        final Set<String> readLaterGuids = new HashSet<>();

        readLaterGuids.addAll(this.readLaterItemIds);
        readGuids.addAll(this.readItemIds);

        return new ReadFeedItems(this.feedId, this.lastUpdate, readGuids, readLaterGuids, categoryId);
    }

}
