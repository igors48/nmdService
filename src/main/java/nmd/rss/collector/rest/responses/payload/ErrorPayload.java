package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.error.ServiceError;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isValidString;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ErrorPayload {

    public ErrorCode code = null;
    public String message = null;
    public String hints = null;

    private ErrorPayload() {
        // empty
    }

    public static ErrorPayload create(final ErrorCode code, final String message, final String hints) {
        guard(notNull(code));
        guard(isValidString(message));
        guard(isValidString(hints));

        final ErrorPayload result = new ErrorPayload();

        result.code = code;
        result.message = message;
        result.hints = hints;

        return result;
    }

    public static ErrorPayload create(final ServiceError error) {
        assertNotNull(error);

        return create(error.code, error.message, error.hints);
    }

}
