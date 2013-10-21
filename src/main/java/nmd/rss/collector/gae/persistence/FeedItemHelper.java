package nmd.rss.collector.gae.persistence;

import nmd.rss.collector.feed.FeedItem;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 21.10.13
 */
public class FeedItemHelper {

    private String title;
    private String description;
    private String link;
    private Date date;
    private String guid;

    private FeedItemHelper() {
        // empty
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public static FeedItemHelper convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        final FeedItemHelper helper = new FeedItemHelper();

        helper.setDate(feedItem.date);
        helper.setDescription(feedItem.description);
        helper.setGuid(feedItem.guid);
        helper.setLink(feedItem.link);
        helper.setTitle(feedItem.title);

        return helper;
    }

    public static FeedItem convert(final FeedItemHelper feedItemHelper) {
        assertNotNull(feedItemHelper);

        final Date date = feedItemHelper.getDate();
        final String description = feedItemHelper.getDescription();
        final String guid = feedItemHelper.getGuid();
        final String link = feedItemHelper.getLink();
        final String title = feedItemHelper.getTitle();

        return new FeedItem(title, description, link, date, guid);
    }

}
