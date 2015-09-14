package nmd.orb.feed;

import nmd.orb.error.ServiceError;

import java.io.Serializable;
import java.util.Date;

import static nmd.orb.error.ServiceError.invalidItemDate;
import static nmd.orb.error.ServiceError.invalidItemGotoLink;
import static nmd.orb.feed.FeedItem.*;
import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class FeedItemShortcut implements Serializable {

    public final String guid;
    public final Date date;
    public final String gotoLink;

    public FeedItemShortcut(final String guid, final Date date, final String gotoLink) {
        guard(isValidFeedItemGuid(this.guid = guid), ServiceError.invalidItemId(guid));
        guard(isValidFeedItemDate(this.date = date), invalidItemDate(date));
        guard(isValidFeedItemLink(this.gotoLink = gotoLink), invalidItemGotoLink(gotoLink));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItemShortcut shortcut = (FeedItemShortcut) o;

        if (!guid.equals(shortcut.guid)) return false;
        if (!date.equals(shortcut.date)) return false;
        return gotoLink.equals(shortcut.gotoLink);

    }

    @Override
    public int hashCode() {
        int result = guid.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + gotoLink.hashCode();
        return result;
    }

}
