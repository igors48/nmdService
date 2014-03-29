package nmd.rss.collector.feed;

import java.io.Serializable;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedHeader implements Serializable {

    public static final int MAX_DESCRIPTION_AND_TITLE_LENGTH = 255;

    public final UUID id;
    public final String feedLink;
    public final String title;
    public final String description;
    public final String link;

    public FeedHeader(final UUID id, final String feedLink, final String title, final String description, final String link) {
        guard(isValidFeedId(id));
        this.id = id;

        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidFeedTitle(title));
        this.title = title;

        guard(isValidFeedDescription(description));
        this.description = description;

        guard(isValidUrl(link));
        this.link = link;
    }

    public FeedHeader changeTitle(final String newTitle) {
        guard(isValidFeedTitle(newTitle));

        return new FeedHeader(this.id, this.feedLink, newTitle, this.description, this.link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedHeader header = (FeedHeader) o;

        if (!description.equals(header.description)) return false;
        if (!feedLink.equals(header.feedLink)) return false;
        if (!id.equals(header.id)) return false;
        if (!link.equals(header.link)) return false;
        if (!title.equals(header.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();

        result = 31 * result + feedLink.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();

        return result;
    }

    public static boolean isValidFeedId(final UUID feedId) {
        return feedId != null;
    }

    public static boolean isValidFeedId(final String feedId) {
        return isValidUuid(feedId);
    }

    public static boolean isValidFeedTitle(final String feedTitle) {
        return isValidString(feedTitle) && feedTitle.length() <= MAX_DESCRIPTION_AND_TITLE_LENGTH;
    }

    public static boolean isValidFeedDescription(final String feedDescription) {
        return isValidString(feedDescription) && feedDescription.length() <= MAX_DESCRIPTION_AND_TITLE_LENGTH;
    }

    public static FeedHeader create(final UUID id, final String feedLink, final String title, final String description, final String link) {

        if (!isValidFeedId(id)) {
            return null;
        }

        if (!isValidUrl(feedLink)) {
            return null;
        }

        if (!isValidFeedTitle(title)) {
            return null;
        }

        if (!isValidFeedDescription(description)) {
            return null;
        }

        if (!isValidUrl(link)) {
            return null;
        }

        return new FeedHeader(id, feedLink, title, description, link);
    }

}
