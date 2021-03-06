package nmd.orb.feed;

import nmd.orb.error.ServiceException;
import nmd.orb.util.IllegalParameterException;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

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
        guard(isValidFeedHeaderId(this.id = id), invalidFeedId(null));
        guard(isValidUrl(this.feedLink = feedLink), invalidFeedUrl(feedLink));
        guard(isValidFeedHeaderTitle(this.title = title), invalidFeedTitle(title));
        guard(isValidFeedHeaderDescription(this.description = description), invalidFeedDescription(description));
        guard(isValidUrl(this.link = link), invalidUrl(link));
    }

    public FeedHeader changeTitle(final String newTitle) {
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

    public static FeedHeader create(final UUID id, final String feedLink, final String title, final String description, final String link) throws ServiceException {

        try {
            return new FeedHeader(id, feedLink, title, description, link);
        } catch (IllegalParameterException exception) {
            throw new ServiceException(exception.serviceError);
        }
    }

}
