package nmd.rss.collector.error;

import java.util.UUID;

import static java.lang.String.format;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.06.13
 */
public class ServiceError {

    public final ErrorCode code;
    public final String message;
    public final String hints;

    private ServiceError(final ErrorCode code, final String message, final String hints) {
        this.code = code;
        this.message = message;
        this.hints = hints;
    }

    public static ServiceError urlFetcherError(final String link) {

        return new ServiceError(ErrorCode.URL_FETCH_ERROR,
                format("Error fetching url [ %s ]", link),
                "Invalid url or host unreachable. Check the url and try again.");
    }

    public static ServiceError feedExportError(final UUID feedId) {
        assertNotNull(feedId);

        return new ServiceError(ErrorCode.FEED_EXPORT_ERROR,
                format("Unable to export feed with id [ %s ]", feedId),
                "Looks like feed data corrupted. Try to recreate this feed.");
    }

    public static ServiceError feedParseError(final String link) {
        assertStringIsValid(link);

        return new ServiceError(ErrorCode.FEED_PARSE_ERROR,
                format("Unable parse feed from [ %s ]", link),
                "Possibly feed data corrupted. Check feed data.");
    }

    public static ServiceError wrongFeedId(final UUID feedId) {
        assertNotNull(feedId);

        return new ServiceError(ErrorCode.WRONG_FEED_ID,
                format("Unable to find feed with id [ %s ]", feedId),
                "Possibly feed id incorrect. Check feed identifier.");
    }

    public static ServiceError invalidFeedId(final String feedId) {

        return new ServiceError(ErrorCode.INVALID_FEED_ID,
                format("Feed id [ %s ] is invalid", feedId),
                "Feed id cannot be parsed. Check feed identifier.");
    }

    public static ServiceError invalidFeedTitle(final String feedTitle) {

        return new ServiceError(ErrorCode.INVALID_FEED_TITLE,
                format("Feed title [ %s ] is invalid", feedTitle),
                "Check feed title.");
    }

    public static ServiceError invalidFeedOrItemId(final String pathInfo) {

        return new ServiceError(ErrorCode.INVALID_FEED_OR_ITEM_ID,
                format("Feed id or item id cannot be parsed from [ %s ] ", pathInfo),
                "Item id cannot be parsed. Check item identifier.");
    }

    public static ServiceError invalidMarkMode(final String markMode) {

        return new ServiceError(ErrorCode.INVALID_MARK_MODE,
                format("Invalid mark mode [ %s ] ", markMode),
                "Mark mode is invalid. Check mark mode.");
    }

    public static ServiceError wrongFeedTaskId(final UUID feedId) {
        assertNotNull(feedId);

        return new ServiceError(ErrorCode.WRONG_FEED_TASK_ID,
                format("Unable to find task for feed with id [ %s ]", feedId),
                "Possibly feed id incorrect. Check feed identifier.");
    }

    public static ServiceError noScheduledTask() {
        return new ServiceError(ErrorCode.NO_SCHEDULED_TASK,
                "There is no task scheduled for update",
                "Possibly feed update schedule is empty. Check registered feeds list.");
    }

    public static ServiceError wrongCategoryId(final String categoryId) {
        return new ServiceError(ErrorCode.WRONG_CATEGORY_ID,
                format("There is no category with id [ %s ]", categoryId),
                "Possibly category id is wrong. Check category id.");
    }

    public static ServiceError categoryAlreadyExists(final String categoryName) {
        return new ServiceError(ErrorCode.CATEGORY_ALREADY_EXISTS,
                format("Category [ %s ] already exists", categoryName),
                "Category with given name already exists. Try to use another name.");
    }

    public static ServiceError invalidCategoryName(final String categoryName) {
        return new ServiceError(ErrorCode.INVALID_CATEGORY_NAME,
                format("Category name [ %s ] invalid", categoryName),
                "Invalid category name. Try to use another name.");
    }

    public static ServiceError invalidFeedUrl(final String feedUrl) {
        return new ServiceError(ErrorCode.INVALID_FEED_URL,
                format("Feed url [ %s ] invalid", feedUrl),
                "Invalid feed url. Check the url.");
    }

    public static ServiceError invalidCategoryId(final String categoryId) {
        return new ServiceError(ErrorCode.INVALID_CATEGORY_ID,
                format("Category identifier [ %s ] invalid", categoryId),
                "Invalid category identifier. Check the identifier.");
    }

    @Override
    public String toString() {
        return format("ServiceError. Code [ %s ], message [ %s ]", this.code, this.message);
    }

}
