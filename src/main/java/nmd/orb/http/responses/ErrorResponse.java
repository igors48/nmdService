package nmd.orb.http.responses;

import nmd.orb.collector.error.ErrorCode;
import nmd.orb.collector.error.ServiceError;
import nmd.orb.http.responses.payload.ErrorPayload;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidString;
import static nmd.orb.util.Parameter.notNull;

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
