package nmd.rss.collector.feed;

import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.Assert.assertValidUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedHeader {

    public final String title;
    public final String description;
    public final String link;

    public FeedHeader(final String title, final String description, final String link) {
        assertStringIsValid(title);
        this.title = title;

        assertStringIsValid(title);
        this.description = description;

        assertValidUrl(link);
        this.link = link;
    }

}
