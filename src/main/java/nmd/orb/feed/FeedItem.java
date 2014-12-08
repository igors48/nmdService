package nmd.orb.feed;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FeedItem.class.getName());

    public static final int MAX_TITLE_LENGTH = 255;
    public static final int MAX_DESCRIPTION_LENGTH = 1024;
    public static final long TWENTY_FOUR_HOURS = 86400000L;
    public static final long FIFTY_YEARS = 1577846298735L;

    private static final int ONE_SECOND = 1000;

    public final String title;
    public final String description;
    public final String link;
    public final String gotoLink;
    public final Date date;
    public final boolean dateReal;
    public final String guid;

    public FeedItem(final String title, final String description, final String link, final String gotoLink, final Date date, final boolean dateReal, final String guid) {
        guard(isValidFeedItemTitle(title));
        this.title = title;

        guard(isValidFeedItemDescription(description));
        this.description = description;

        guard(isValidFeedItemLink(link));
        this.link = link;

        if (!isValidFeedItemLink(gotoLink)) {
            LOGGER.log(Level.SEVERE, String.format("Item link [ %s ] Invalid goto link [ %s ]", link, gotoLink));
        }

        guard(isValidFeedItemLink(gotoLink));
        this.gotoLink = gotoLink;

        guard(isValidFeedItemDate(date));
        this.date = date;

        this.dateReal = dateReal;

        guard(isValidFeedItemGuid(guid));
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItem feedItem = (FeedItem) o;

        if (dateReal != feedItem.dateReal) return false;

        if (dateReal) {
            if (Math.abs(date.getTime() - feedItem.date.getTime()) > ONE_SECOND) return false;
        }

        if (!description.equals(feedItem.description)) return false;
        if (!gotoLink.equals(feedItem.gotoLink)) return false;
        if (!guid.equals(feedItem.guid)) return false;
        if (!link.equals(feedItem.link)) return false;
        if (!title.equals(feedItem.title)) return false;

        return true;
    }

    public boolean equalsExcludeGuid(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedItem feedItem = (FeedItem) o;

        if (dateReal != feedItem.dateReal) return false;

        if (dateReal) {
            if (Math.abs(date.getTime() - feedItem.date.getTime()) > ONE_SECOND) return false;
        }

        if (!description.equals(feedItem.description)) return false;
        if (!gotoLink.equals(feedItem.gotoLink)) return false;

        if (!link.equals(feedItem.link)) return false;
        if (!title.equals(feedItem.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + gotoLink.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (dateReal ? 1 : 0);
        result = 31 * result + guid.hashCode();
        return result;
    }

    public static boolean isValidFeedItemTitle(final String value) {
        return notNull(value) && value.length() <= MAX_TITLE_LENGTH;
    }

    public static boolean isValidFeedItemDescription(final String value) {
        return notNull(value) && value.length() <= MAX_DESCRIPTION_LENGTH;
    }

    public static boolean isValidFeedItemLink(final String value) {
        return isValidUrl(value);
    }

    public static boolean isValidFeedItemDate(final Date value) {
        return notNull(value);
    }

    public static boolean isValidFeedItemGuid(final String value) {
        return isValidString(value);
    }

    public static boolean isDateReal(final Date date, final Date current) {
        guard(notNull(current));

        if (date == null) {
            return false;
        }

        final boolean dateFromFarPast = (current.getTime() - date.getTime()) > FIFTY_YEARS;
        final boolean dateFromFarFuture = (date.getTime() - current.getTime()) > TWENTY_FOUR_HOURS;

        return !(dateFromFarPast || dateFromFarFuture);
    }

}
