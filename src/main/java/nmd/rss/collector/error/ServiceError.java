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

    public final int code;
    public final String message;
    public final String hints;

    private ServiceError(final int code, final String message, final String hints) {
        this.code = code;
        this.message = message;
        this.hints = hints;
    }

    public static ServiceError urlFetcherError(final String link) {
        assertStringIsValid(link);

        return new ServiceError(1,
                format("Error fetching url [ %s ]", link),
                "Invalid url or host unreachable. Check the url and try again.");
    }

    public static ServiceError feedExportError(final UUID feedId) {
        assertNotNull(feedId);

        return new ServiceError(2,
                format("Unable to export feed with id [ %s ]", feedId),
                "Looks like feed data corrupted. Try to recreate this feed.");
    }

    public static ServiceError feedParseError(final String link) {
        assertStringIsValid(link);

        return new ServiceError(3,
                format("Unable parse feed from [ %s ]", link),
                "Possibly feed data corrupted. Check feed data.");
    }

    public static ServiceError wrongFeedId(final UUID feedId) {
        assertNotNull(feedId);

        return new ServiceError(4,
                format("Unable to find feed with id [ %s ]", feedId),
                "Possibly feed id incorrect. Check feed identifier.");
    }

    public static ServiceError invalidFeedId() {

        return new ServiceError(5,
                "Unable to parse feed id from request",
                "Looks like feed id not in valid UUID format");
    }

}
