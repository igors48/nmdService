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

    public FeedHeader(String title, String description, String link) {
        assertStringIsValid(title);
        this.title = title;

        assertStringIsValid(title);
        this.description = description;

        assertValidUrl(link);
        this.link = link;
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

}
