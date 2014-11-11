package sandbox;

import com.google.gson.Gson;

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
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 11.11.2014
 */
public class instagram {

    private static final Gson GSON = new Gson();

    private static final String UTF_8 = "UTF-8";
    private static final String GET = "GET";

    private static final String USER_ID = "325048554";
    //private static final String USER_ID = "239641091";
    //private static final String USER_ID = "338763452";
    private static final String CLIENT_ID = "b2f1ccbaed2642efb28e5710f652ca85";
    private static final String RECENT_MEDIA_URL_TEMPLATE = "https://api.instagram.com/v1/users/%s/media/recent/?client_id=%s";

    public static void main(String[] arguments) throws IOException {
        //final URL url = new URL("https://api.instagram.com/v1/users/search?q=kagarek&client_id=b2f1ccbaed2642efb28e5710f652ca85");
        final URL url = new URL(format(RECENT_MEDIA_URL_TEMPLATE, USER_ID, CLIENT_ID));

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(GET);

        final String content = readAllFromConnection(connection);

        final ContentEnvelope contentEnvelope = GSON.fromJson(content, ContentEnvelope.class);
        final UserEnvelope userEnvelope = GSON.fromJson(content, UserEnvelope.class);

        System.out.println(content);
        System.out.println(contentEnvelope);
        System.out.println(userEnvelope);
    }

    public static String readAllFromConnection(final HttpURLConnection connection) throws IOException {
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
