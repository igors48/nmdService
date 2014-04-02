package nmd.rss.collector.rest.responses;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.rest.responses.payload.ErrorPayload;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ErrorResponse extends BaseResponse {

    private ErrorPayload error;

    private ErrorResponse() {
        // empty
    }

    public ErrorCode getCode() {
        return this.error.code;
    }

    public static ErrorResponse create(final ErrorCode code, final String message, final String hints) {
        assertNotNull(code);
        assertStringIsValid(message);
        assertStringIsValid(hints);

        final ErrorResponse result = new ErrorResponse();

        result.status = ResponseType.ERROR;

        result.error = ErrorPayload.create(code, message, hints);

        return result;
    }

    public static ErrorResponse create(final ServiceError error) {
        assertNotNull(error);

        return create(error.code, error.message, error.hints);
    }

}
