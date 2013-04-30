package nmd.rss.collector;

import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.collector.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem {

    public final String title;
    public final String description;
    public final String link;
    public final long timestamp;

    public FeedItem(final String title, final String description, final String link, final long timestamp) {
        assertNotNull(title);
        assertNotNull(description);
        assertTrue(isValidString(title) || isValidString(description));
        this.title = title;
        this.description = description;

        assertValidUrl(link);
        this.link = link;

        assertPositive(timestamp);
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FeedItem item = (FeedItem) o;

        if (timestamp != item.timestamp) return false;

        if (!description.equals(item.description)) return false;

        if (!link.equals(item.link)) return false;

        if (!title.equals(item.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();

        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));

        return result;
    }
}
