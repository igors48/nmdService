package nmd.orb.collector.feed;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.*;

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
        guard(isValidFeedHeaderId(id));
        this.id = id;

        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidFeedHeaderTitle(title));
        this.title = title;

        guard(isValidFeedHeaderDescription(description));
        this.description = description;

        guard(isValidUrl(link));
        this.link = link;
    }

    public FeedHeader changeTitle(final String newTitle) {
        guard(isValidFeedHeaderTitle(newTitle));

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

    public static boolean isValidFeedHeaderId(final UUID feedId) {
        return feedId != null;
    }

    public static boolean isValidFeedHeaderId(final String feedId) {
        return isValidUuid(feedId);
    }

    public static boolean isValidFeedHeaderTitle(final String feedTitle) {
        return isValidString(feedTitle) && feedTitle.length() <= MAX_DESCRIPTION_AND_TITLE_LENGTH;
    }

    public static boolean isValidFeedHeaderDescription(final String feedDescription) {
        return notNull(feedDescription) && feedDescription.length() <= MAX_DESCRIPTION_AND_TITLE_LENGTH;
    }

    public static FeedHeader create(final UUID id, final String feedLink, final String title, final String description, final String link) {

        if (!isValidFeedHeaderId(id)) {
            return null;
        }

        if (!isValidUrl(feedLink)) {
            return null;
        }

        if (!isValidFeedHeaderTitle(title)) {
            return null;
        }

        if (!isValidFeedHeaderDescription(description)) {
            return null;
        }

        if (!isValidUrl(link)) {
            return null;
        }

        return new FeedHeader(id, feedLink, title, description, link);
    }

}
