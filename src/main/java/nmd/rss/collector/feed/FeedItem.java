package nmd.rss.collector.feed;

import java.io.Serializable;
import java.util.Date;

import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.collector.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedItem implements Serializable {

    private static final int ONE_SECOND = 1000;

    public final String title;
    public final String description;
    public final String link;
    public final Date date;
    public final boolean dateReal;
    public final String guid;

    public FeedItem(final String title, final String description, final String link, final Date date, final boolean dateReal, final String guid) {
        assertNotNull(title);
        assertNotNull(description);
        guard(isValidString(title) || isValidString(description));
        this.title = title;
        this.description = description;

        assertValidUrl(link);
        this.link = link;

        assertNotNull(date);
        this.date = date;

        this.dateReal = dateReal;

        assertStringIsValid(guid);
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
        if (!link.equals(feedItem.link)) return false;
        if (!title.equals(feedItem.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (dateReal ? 1 : 0);
        result = 31 * result + guid.hashCode();
        return result;
    }
}
