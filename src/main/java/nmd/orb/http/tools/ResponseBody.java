package nmd.orb.http.tools;

import com.google.gson.Gson;
import nmd.orb.collector.error.ServiceError;
import nmd.orb.collector.error.ServiceException;
import nmd.orb.http.responses.ErrorResponse;

import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 24.06.13
 */
public class ResponseBody {

    private static final Gson GSON = new Gson();

    public final ContentType contentType;
    public final String content;

    public ResponseBody(final ContentType contentType, final String content) {
        assertNotNull(contentType);
        this.contentType = contentType;

        assertStringIsValid(content);
        this.content = content;
    }

    public static ResponseBody createJsonResponse(final Object object) {
        final String content = GSON.toJson(object);

        return new ResponseBody(ContentType.JSON, content);
    }

    public static ResponseBody createErrorJsonResponse(final ServiceException exception) {
        final ServiceError error = exception.getError();

        return createErrorJsonResponse(error);
    }

    public static ResponseBody createErrorJsonResponse(final ServiceError error) {
        final ErrorResponse errorResponse = ErrorResponse.create(error);

        return createJsonResponse(errorResponse);
    }

}
