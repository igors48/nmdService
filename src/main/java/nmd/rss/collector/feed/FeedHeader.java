package nmd.rss.collector.feed;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedHeader {

    public final UUID id;
    public final String title;
    public final String description;
    public final String link;

    public FeedHeader(final UUID id, final String title, final String description, final String link) {
        assertNotNull(id);
        this.id = id;

        assertStringIsValid(title);
        this.title = title;

        assertStringIsValid(title);
        this.description = description;

        assertValidUrl(link);
        this.link = link;
    }

}
