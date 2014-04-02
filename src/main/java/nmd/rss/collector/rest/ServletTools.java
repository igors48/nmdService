package nmd.rss.collector.rest;

import com.google.gson.Gson;
import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.requests.AddFeedRequest;
import nmd.rss.collector.rest.responses.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public final class ServletTools {

    private static final Logger LOGGER = Logger.getLogger(ServletTools.class.getName());

    private static final String UTF_8 = "UTF-8";
    private static final Gson GSON = new Gson();

    public static UUID parseFeedId(final String pathInfo) {

        if (pathInfoIsEmpty(pathInfo)) {
            return null;
        }

        try {
            final String data = pathInfo.substring(1);

            return UUID.fromString(data);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error parse feedId from [ %s ]", pathInfo), exception);

            return null;
        }
    }

    public static FeedAndItemIds parseFeedAndItemIds(final String pathInfo) {

        try {
            final List<String> elements = parse(pathInfo);

            final UUID feedId = UUID.fromString(elements.get(0));
            final String itemId = elements.size() > 1 ? elements.get(1) : "";

            return new FeedAndItemIds(feedId, itemId);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error parse feedId and itemId from [ %s ]", pathInfo), exception);

            return null;
        }
    }

    public static boolean pathInfoIsEmpty(final String pathInfo) {
        return pathInfo == null || pathInfo.length() < 2;
    }

    public static String readRequestBody(final HttpServletRequest request) {
        assertNotNull(request);

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


    public static void writeResponseBody(final ResponseBody responseBody, final HttpServletResponse response) throws IOException {
        assertNotNull(responseBody);
        assertNotNull(response);

        response.setContentType(responseBody.contentType.mime);
        response.setCharacterEncoding(UTF_8);

        response.getWriter().print(responseBody.content);
    }

    public static void writeException(final Exception exception, final HttpServletResponse response) {
        assertNotNull(exception);
        assertNotNull(response);

        String message = exception.getMessage();
        message = message == null || message.isEmpty() ? exception.getClass().getSimpleName() : message;

        //TODO get more information from exception
        final ErrorResponse errorResponse = ErrorResponse.create(ErrorCode.UNHANDLED_EXCEPTION, message, "Please try again later");
        final String content = GSON.toJson(errorResponse);
        final ResponseBody responseBody = new ResponseBody(ContentType.JSON, content);

        try {
            writeResponseBody(responseBody, response);
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

    private ServletTools() {
        // empty
    }

}
