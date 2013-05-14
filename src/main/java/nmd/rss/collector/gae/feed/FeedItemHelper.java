package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedItem;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class FeedItemHelper {

    private UUID id;
    private String title;
    private String description;
    private String link;
    private long timestamp;

    private FeedItemHelper(final UUID id, final String title, final String description, final String link, final long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.timestamp = timestamp;
    }

    private FeedItemHelper() {
        // empty
    }

    public static FeedItemHelper convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        return new FeedItemHelper(feedItem.id, feedItem.title, feedItem.description, feedItem.link, feedItem.timestamp);
    }

    public static FeedItem convert(final FeedItemHelper feedItemHelper) {
        assertNotNull(feedItemHelper);

        return new FeedItem(feedItemHelper.id, feedItemHelper.title, feedItemHelper.description, feedItemHelper.link, feedItemHelper.timestamp);
    }

}
