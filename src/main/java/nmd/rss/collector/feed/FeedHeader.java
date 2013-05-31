package nmd.rss.collector.feed;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedHeader {

    public final UUID id;
    public final String feedLink;
    public final String title;
    public final String description;
    public final String link;

    public FeedHeader(final UUID id, final String feedLink, final String title, final String description, final String link) {
        assertNotNull(id);
        this.id = id;

        assertValidUrl(feedLink);
        this.feedLink = feedLink;

        assertStringIsValid(title);
        this.title = title;

        assertStringIsValid(title);
        this.description = description;

        assertValidUrl(link);
        this.link = link;
    }

}
