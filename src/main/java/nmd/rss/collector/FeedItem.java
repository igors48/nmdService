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

}
