package nmd.rss.collector;

import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.collector.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem {

    private final String title;
    private final String description;
    private final String link;
    private final long timestamp;

    public FeedItem(final String title, final String description, final String link, final long timestamp) {
        assertTrue(isValidString(title) || isValidString(description));
        this.title = title;
        this.description = description;

        assertValidUrl(link);
        this.link = link;

        assertPositive(timestamp);
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

}
