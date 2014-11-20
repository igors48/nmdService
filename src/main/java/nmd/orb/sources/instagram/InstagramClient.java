package nmd.orb.sources.instagram;

import com.google.gson.Gson;
import nmd.orb.sources.instagram.entities.ContentEnvelope;
import nmd.orb.sources.instagram.entities.UserEnvelope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.format;
import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.CloseableTools.close;

/**
 * @author : igu
 */
public class InstagramClient {

    public static final String CLIENT_ID = System.getProperty("instagram.clientId");
    //public static final String CLIENT_ID = "b2f1ccbaed2642efb28e5710f652ca85";

    private static final Gson GSON = new Gson();

    private static final String UTF_8 = "UTF-8";
    private static final String GET = "GET";

    private static final String RECENT_MEDIA_URL_TEMPLATE = "https://api.instagram.com/v1/users/%s/media/recent/?client_id=%s";
    private static final String SEARCH_USER_URL_TEMPLATE = "https://api.instagram.com/v1/users/search?q=%s&client_id=%s";

    public static UserEnvelope searchUsers(final String mask, final String clientId) throws IOException {
        final String link = format(SEARCH_USER_URL_TEMPLATE, mask, clientId);
        final String content = getContent(link);

        return GSON.fromJson(content, UserEnvelope.class);
    }

    public static ContentEnvelope fetchRecentMedia(final String userId, final String clientId) throws IOException {
        final String link = format(RECENT_MEDIA_URL_TEMPLATE, userId, clientId);
        final String content = getContent(link);

        return GSON.fromJson(content, ContentEnvelope.class);
    }

    private static String getContent(final String link) throws IOException {
        final HttpURLConnection connection = setupConnection(link);

        return readAllFromConnection(connection);
    }

    private static HttpURLConnection setupConnection(final String link) throws IOException {
        final URL url = new URL(link);

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(GET);

        return connection;
    }

    private static String readAllFromConnection(final HttpURLConnection connection) throws IOException {
        assertNotNull(connection);

        final InputStream inputStream = connection.getInputStream();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            inputStreamReader = new InputStreamReader(inputStream, UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);

            final StringBuilder result = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } finally {
            close(bufferedReader);
            close(inputStreamReader);
            close(inputStream);
        }
    }

}
