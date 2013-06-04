package nmd.rss.collector.gae.feed.header;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.util.Assert;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class FeedHeaderHelper {

    private UUID id;
    private String feedLink;
    private String title;
    private String description;
    private String link;

    private FeedHeaderHelper() {
        // empty
    }

    public FeedHeaderHelper(final UUID id, final String feedLink, final String title, final String description, final String link) {
        this.id = id;
        this.feedLink = feedLink;
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public static FeedHeader convert(final FeedHeaderHelper feedHeaderHelper) {
        Assert.assertNotNull(feedHeaderHelper);

        return new FeedHeader(feedHeaderHelper.id, feedHeaderHelper.feedLink, feedHeaderHelper.title, feedHeaderHelper.description, feedHeaderHelper.link);
    }

    public static FeedHeaderHelper convert(final FeedHeader feedHeader) {
        Assert.assertNotNull(feedHeader);

        return new FeedHeaderHelper(feedHeader.id, feedHeader.feedLink, feedHeader.title, feedHeader.description, feedHeader.link);
    }

}
