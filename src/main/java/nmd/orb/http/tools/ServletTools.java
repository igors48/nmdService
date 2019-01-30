package nmd.orb.http.tools;

import com.google.gson.Gson;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.requests.AddFeedRequest;
import nmd.orb.http.responses.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CloseableTools.close;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public final class ServletTools {

    private static final Logger LOGGER = Logger.getLogger(ServletTools.class.getName());

    public static final String SERVER_PROCESSING_TIME_HEADER = "Orb-Server-Processing-Time";

    private static final String UTF_8 = "UTF-8";

    private static final Gson GSON = new Gson();

    public static UUID parseUuid(final String string) {

        try {
            return UUID.fromString(string);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Integer parseInteger(final String string) {

        try {
            return Integer.valueOf(string);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Long parseLong(final String string) {

        try {
            return Long.valueOf(string);
        } catch (Exception exception) {
            return null;
        }
    }

    public static UUID parseFeedId(final String data) {

        if (pathInfoIsEmpty(data)) {
            return null;
        }

        try {
            return UUID.fromString(data);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error parse feedId from [ %s ]", data), exception);

            return null;
        }
    }

    public static FeedAndItemIds parseFeedAndItemIds(final List<String> elements) {

        try {
            final UUID feedId = UUID.fromString(elements.get(0));
            final String itemId = elements.size() > 1 ? elements.get(1) : "";

            return new FeedAndItemIds(feedId, itemId);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error parse feedId and itemId from [ %s ]", elements), exception);

            return null;
        }
    }

    public static boolean pathInfoIsEmpty(final String pathInfo) {
        return pathInfo == null || pathInfo.length() < 2;
    }

    public static String readRequestBody(final HttpServletRequest request) {
        guard(notNull(request));

        BufferedReader reader = null;

        try {
            reader = request.getReader();
            final StringBuilder result = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString().trim();
        } catch (IOException e) {
            return "";
        } finally {
            close(reader);
        }
    }

    public static AddFeedRequest convert(final String requestBody) {

        try {
            return GSON.fromJson(requestBody, AddFeedRequest.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error reading add feed request body", exception);

            return null;
        }
    }


    public static void writeResponseBody(final long startTime, final ResponseBody responseBody, final HttpServletResponse response) throws IOException {
        guard(isPositive(startTime));
        guard(notNull(responseBody));
        guard(notNull(response));

        response.setContentType(responseBody.contentType.mime);
        //response.setContentLength(responseBody.content.length());
        //response.setCharacterEncoding(UTF_8);

        if (!responseBody.fileName.isEmpty()) {
            response.setHeader("Content-Transfer-Encoding", "Binary");
            response.setHeader("Content-Disposition", format("attachment;filename=\"%s\"", responseBody.fileName));
        }

        final long processingTime = System.currentTimeMillis() - startTime;
        response.setHeader(SERVER_PROCESSING_TIME_HEADER, valueOf(processingTime));

        final PrintWriter writer = response.getWriter();
        writer.print(responseBody.content);
        writer.flush();
    }

    public static void writeException(final long startTime, final Exception exception, final HttpServletResponse response) {
        guard(isPositive(startTime));
        guard(notNull(exception));
        guard(notNull(response));

        String message = exception.getMessage();
        message = message == null || message.isEmpty() ? exception.getClass().getSimpleName() : message;

        //TODO get more information from exception
        final ErrorResponse errorResponse = ErrorResponse.create(ErrorCode.UNHANDLED_EXCEPTION, message, "Please try again later");
        final String content = GSON.toJson(errorResponse);
        final ResponseBody responseBody = new ResponseBody(ContentType.JSON, content);

        try {
            writeResponseBody(startTime, responseBody, response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static List<String> parse(final String pathInfo) {
        final List<String> elements = new ArrayList<>();

        if (pathInfo == null) {
            return elements;
        }

        String copy = pathInfo.trim();

        int slashIndex = copy.indexOf("/");

        while (slashIndex != -1) {
            final String element = copy.substring(0, slashIndex);

            if (!element.isEmpty()) {
                elements.add(element);
            }

            copy = copy.substring(slashIndex + 1).trim();

            slashIndex = copy.indexOf("/");
        }

        if (!copy.isEmpty()) {
            elements.add(copy);
        }

        return elements;
    }

    public static Map<String, String> convert(final Map requestParameters) {
        guard(notNull(requestParameters));

        final Map<String, String> result = new HashMap<>();

        for (final Object key : requestParameters.keySet()) {
            final String keyAsString = (String) key;
            final Object[] values = (Object[]) requestParameters.get(key);
            final String valueAsString = values.length == 0 ? "" : (String) values[0];

            result.put(keyAsString, valueAsString);
        }

        return result;
    }

    private ServletTools() {
        // empty
    }

}
