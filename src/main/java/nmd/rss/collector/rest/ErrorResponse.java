package nmd.rss.collector.rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.error.ServiceError;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class ErrorResponse extends BaseResponse {

    private ErrorCode code;
    private String message;
    private String hints;

    private ErrorResponse() {
        // empty
    }

    protected ErrorResponse(final ErrorCode code, final String message, final String hints) {
        super();

        setStatus(ResponseType.ERROR);
        setCode(code);
        setMessage(message);
        setHints(hints);
    }

    protected ErrorResponse(final ServiceError error) {
        this(error.code, error.message, error.hints);
    }

    ErrorCode getCode() {
        return this.code;
    }

    void setCode(final ErrorCode code) {
        assertNotNull(code);
        this.code = code;
    }

    String getMessage() {
        return this.message;
    }

    void setMessage(final String message) {
        assertStringIsValid(message);
        this.message = message;
    }

    String getHints() {
        return this.hints;
    }

    void setHints(final String hints) {
        assertStringIsValid(hints);
        this.hints = hints;
    }

}
