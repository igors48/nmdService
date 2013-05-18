package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedItem;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class FeedItemHelper {

    private String title;
    private String description;
    private String link;
    private Date date;

    private FeedItemHelper(final String title, final String description, final String link, final Date date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    private FeedItemHelper() {
        // empty
    }

    public static FeedItemHelper convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        return new FeedItemHelper(feedItem.title, feedItem.description, feedItem.link, feedItem.date);
    }

    public static FeedItem convert(final FeedItemHelper feedItemHelper) {
        assertNotNull(feedItemHelper);

        return new FeedItem(feedItemHelper.title, feedItemHelper.description, feedItemHelper.link, feedItemHelper.date);
    }

}
