package nmd.orb.gae.repositories.converters.helpers;

import com.google.appengine.api.datastore.Text;
import nmd.orb.feed.FeedItem;

import java.util.Date;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 21.10.13
 */
public class FeedItemHelper {

    private String title;
    private Text description;
    private String link;
    private String gotoLink;
    private Date date;
    private boolean dateReal = true;
    private String guid;

    private FeedItemHelper() {
        // empty
    }

    public static FeedItemHelper convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        final FeedItemHelper helper = new FeedItemHelper();

        helper.date = feedItem.date;
        helper.dateReal = feedItem.dateReal;
        helper.description = new Text(feedItem.description);
        helper.guid = feedItem.guid;
        helper.link = feedItem.link;
        helper.gotoLink = feedItem.gotoLink;
        helper.title = feedItem.title;

        return helper;
    }

    public static FeedItem convert(final FeedItemHelper feedItemHelper) {
        assertNotNull(feedItemHelper);

        final Date date = feedItemHelper.date;
        final boolean dateReal = feedItemHelper.dateReal;
        final String description = feedItemHelper.description.getValue();
        final String guid = feedItemHelper.guid;
        final String link = feedItemHelper.link;
        final String title = feedItemHelper.title;

        final String gotoLink = (feedItemHelper.gotoLink == null || feedItemHelper.gotoLink.isEmpty()) ? feedItemHelper.link : feedItemHelper.gotoLink;

        return new FeedItem(title, description, link, gotoLink, date, dateReal, guid);
    }

}
