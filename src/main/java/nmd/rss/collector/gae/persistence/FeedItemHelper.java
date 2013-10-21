package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Text;
import nmd.rss.collector.feed.FeedItem;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 21.10.13
 */
public class FeedItemHelper {

    private String title;
    private Text description;
    private String link;
    private Date date;
    private String guid;

    private FeedItemHelper() {
        // empty
    }

    public static FeedItemHelper convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        final FeedItemHelper helper = new FeedItemHelper();

        helper.date = feedItem.date;
        helper.description = new Text(feedItem.description);
        helper.guid = feedItem.guid;
        helper.link = feedItem.link;
        helper.title = feedItem.title;

        return helper;
    }

    public static FeedItem convert(final FeedItemHelper feedItemHelper) {
        assertNotNull(feedItemHelper);

        final Date date = feedItemHelper.date;
        final String description = feedItemHelper.description.getValue();
        final String guid = feedItemHelper.guid;
        final String link = feedItemHelper.link;
        final String title = feedItemHelper.title;

        return new FeedItem(title, description, link, date, guid);
    }

}
