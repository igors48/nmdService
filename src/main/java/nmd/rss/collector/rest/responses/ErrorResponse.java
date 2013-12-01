package nmd.rss.collector.rest.responses;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.error.ServiceError;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ErrorResponse extends BaseResponse {

    private ErrorCode code = null;
    private String message = null;
    private String hints = null;

    private ErrorResponse() {
        // empty
    }

    public ErrorCode getCode() {
        return this.code;
    }

    public static ErrorResponse create(final ErrorCode code, final String message, final String hints) {
        assertNotNull(code);
        assertStringIsValid(message);
        assertStringIsValid(hints);

        final ErrorResponse result = new ErrorResponse();

        result.status = ResponseType.ERROR;
        result.code = code;
        result.message = message;
        result.hints = hints;

        return result;
    }

    public static ErrorResponse create(final ServiceError error) {
        assertNotNull(error);

        return create(error.code, error.message, error.hints);
    }

}
