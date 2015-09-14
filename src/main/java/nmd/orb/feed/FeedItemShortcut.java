package nmd.orb.feed;

import nmd.orb.error.ServiceError;

import java.util.Date;

import static nmd.orb.error.ServiceError.invalidItemDate;
import static nmd.orb.error.ServiceError.invalidItemGotoLink;
import static nmd.orb.feed.FeedItem.isValidFeedItemDate;
import static nmd.orb.feed.FeedItem.isValidFeedItemGuid;
import static nmd.orb.feed.FeedItem.isValidFeedItemLink;
import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class FeedItemShortcut {

    public final String guid;
    public final Date date;
    public final String gotoLink;

    public FeedItemShortcut(final String guid, final Date date, final String gotoLink) {
        guard(isValidFeedItemGuid(this.guid = guid), ServiceError.invalidItemId(guid));
        guard(isValidFeedItemDate(this.date = date), invalidItemDate(date));
        guard(isValidFeedItemLink(this.gotoLink = gotoLink), invalidItemGotoLink(gotoLink));
    }

}
