package nmd.orb.feed;

import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.util.IllegalParameterException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem implements Serializable {

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
        guard(isValidFeedItemTitle(this.title = title), invalidItemTitle(title));
        guard(isValidFeedItemDescription(this.description = description), invalidItemDescription(description));
        guard(isValidFeedItemLink(this.link = link), invalidItemLink(link));
        guard(isValidFeedItemLink(this.gotoLink = gotoLink), invalidItemGotoLink(gotoLink));
        guard(isValidFeedItemDate(this.date = date), invalidItemDate(date));
        this.dateReal = dateReal;
        guard(isValidFeedItemGuid(this.guid = guid), ServiceError.invalidItemId(guid));
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

    public static boolean isValidLastUsedFeedItemGuid(final String value) {
        return value != null && (value.isEmpty() || isValidFeedItemGuid(value));
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

    public static FeedItem create(final String title, final String description, final String link, final String gotoLink, final Date date, final boolean dateReal, final String guid) throws ServiceException {

        try {
            return new FeedItem(title, description, link, gotoLink, date, dateReal, guid);
        } catch (IllegalParameterException exception) {
            throw new ServiceException(exception.serviceError);
        }
    }

    public static FeedItemShortcut createShortcut(final FeedItem item) {
        guard(notNull(item));

        return new FeedItemShortcut(item.guid, item.date, item.gotoLink);
    }

    public static List<FeedItemShortcut> createShortcuts(final List<FeedItem> items) {

        if (items == null) {
            return null;
        }

        final List<FeedItemShortcut> shortcuts = new ArrayList<>(items.size());

        for (final FeedItem item : items) {
            final FeedItemShortcut shortcut = createShortcut(item);

            shortcuts.add(shortcut);
        }

        return shortcuts;
    }

}
