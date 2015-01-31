package unit.handler;

import com.google.gson.Gson;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.Handler;
import nmd.orb.http.responses.ErrorResponse;
import nmd.orb.http.responses.ResponseType;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.tools.ServletTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 17.01.2015.
 */
public final class Tools {

    private static final Gson GSON = new Gson();

    public static void assertError(final ResponseBody responseBody, final ErrorCode errorCode) {
        final ErrorResponse errorResponse = GSON.fromJson(responseBody.content, ErrorResponse.class);

        assertEquals(ResponseType.ERROR, errorResponse.getStatus());
        assertEquals(errorCode, errorResponse.error.code);
    }

    public static ResponseBody call(final Handler handler, final String url) {
        return call(handler, url, null);
    }

    public static ResponseBody call(final Handler handler, final String url, final Object body) {
        final String trimmed = url.trim();
        final int questionIndex = trimmed.indexOf("?");

        final String path;
        final String parameters;

        if (questionIndex == -1) {
            path = trimmed;
            parameters = "";
        } else {
            path = trimmed.substring(0, questionIndex);
            parameters = trimmed.substring(questionIndex);
        }

        final List<String> elements = ServletTools.parse(path);
        final Map<String, String> parametersMap = parseParameters(parameters);

        final String bodyAsString = (body instanceof String) ? body.toString() : GSON.toJson(body);

        return handler.handle(elements, parametersMap, body == null ? "" : bodyAsString);
    }

    private static Map<String, String> parseParameters(final String parameters) {
        final Map<String, String> result = new HashMap<>();

        final String[] pairs = parameters.split("&");

        for (final String pair : pairs) {
            final String[] split = pair.split("=");

            if (split.length == 2) {
                result.put(split[0], split[1]);
            }
        }

        return result;
    }

    private Tools() {
        // empty
    }

}
