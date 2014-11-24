package nmd.orb.sources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CloseableTools.close;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ConnectionTools {

    public static enum Method {

        GET("GET"),
        POST("POST");

        public final String method;

        Method(final String method) {
            this.method = method;
        }
    }

    private static final String UTF_8 = "UTF-8";

    public static HttpURLConnection setupConnection(final String link, final Method method) throws IOException {
        guard(isValidUrl(link));
        guard(notNull(method));

        final URL url = new URL(link);

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method.method);

        return connection;
    }

    public static String readAllFromConnection(final HttpURLConnection connection) throws IOException {
        guard(notNull(connection));

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

    private ConnectionTools() {
        // empty
    }

}
