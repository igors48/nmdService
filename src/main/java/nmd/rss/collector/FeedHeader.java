package nmd.rss.collector;

import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.Assert.assertValidUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedHeader {

    private final String title;
    private final String description;
    private final String link;

    public FeedHeader(final String title, final String description, final String link) {
        assertStringIsValid(title);
        this.title = title;

        assertStringIsValid(title);
        this.description = description;

        assertValidUrl(link);
        this.link = link;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getDescription() {
        return this.description;
    }

    public final String getLink() {
        return this.link;
    }

}
