package nmd.rss.collector.feed;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.collector.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem {

    public final UUID id;
    public final String title;
    public final String description;
    public final String link;
    public final long timestamp;

    public FeedItem(final UUID id, final String title, final String description, final String link, final long timestamp) {
        assertNotNull(id);
        this.id = id;

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

    public boolean sameAs(FeedItem feedItem) {
        return timestamp == feedItem.timestamp
                && description.equals(feedItem.description)
                && link.equals(feedItem.link)
                && title.equals(feedItem.title);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItem feedItem = (FeedItem) o;

        if (timestamp != feedItem.timestamp) return false;
        if (!description.equals(feedItem.description)) return false;
        if (!id.equals(feedItem.id)) return false;
        if (!link.equals(feedItem.link)) return false;
        if (!title.equals(feedItem.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();

        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));

        return result;
    }
}
