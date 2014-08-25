package nmd.orb.http.responses.payload;

import nmd.orb.collector.error.ErrorCode;
import nmd.orb.collector.error.ServiceError;

import static nmd.orb.collector.util.Assert.assertNotNull;
import static nmd.orb.collector.util.Assert.guard;
import static nmd.orb.collector.util.Parameter.isValidString;
import static nmd.orb.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ErrorPayload {

    public ErrorCode code;
    public String message;
    public String hints;

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
