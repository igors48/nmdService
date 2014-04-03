package nmd.rss.collector.rest.responses;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.rest.responses.payload.ErrorPayload;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isValidString;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ErrorResponse extends BaseResponse {

    public ErrorPayload error;

    private ErrorResponse() {
        // empty
    }

    public static ErrorResponse create(final ErrorCode code, final String message, final String hints) {
        guard(notNull(code));
        guard(isValidString(message));
        guard(isValidString(hints));

        final ErrorResponse result = new ErrorResponse();

        result.status = ResponseType.ERROR;

        result.error = ErrorPayload.create(code, message, hints);

        return result;
    }

    public static ErrorResponse create(final ServiceError error) {
        guard(notNull(error));

        return create(error.code, error.message, error.hints);
    }

}
