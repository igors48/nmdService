package nmd.orb.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CloseableTools.close;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ConnectionTools {

    public static final String CONTENT_ENCODING = "content-encoding";
    public static final String GZIP = "gzip";

    public static enum Method {

        GET("GET"),
        POST("POST");

        public final String method;

        Method(final String method) {
            this.method = method;
        }
    }

    private static final String UTF_8 = "UTF-8";
    private static final int BUFFER_LENGTH = 16384;

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

    public static boolean ifGZipped(final Map<String, List<String>> headers) {
        guard(notNull(headers));

        for (final String header : headers.keySet()) {

            if (header.equalsIgnoreCase(CONTENT_ENCODING)) {
                final List<String> values = headers.get(header);

                for (final String value : values) {

                    if (value.equalsIgnoreCase(GZIP)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static byte[] readFullyAsUncompressed(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        final byte[] data = new byte[BUFFER_LENGTH];

        int read;

        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public static byte[] readFullyAsGZipped(final InputStream inputStream) throws IOException {
        final byte[] compressed = readFullyAsUncompressed(inputStream);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed);

        GZIPInputStream gZipInputStream = null;

        try {
            gZipInputStream = new GZIPInputStream(byteArrayInputStream);

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            final byte data[] = new byte[BUFFER_LENGTH];

            int result = 0;

            while (result >= 0) {
                result = gZipInputStream.read(data, 0, data.length);

                if (result > 0) {
                    buffer.write(data, 0, result);
                }
            }

            buffer.flush();

            return buffer.toByteArray();
        } finally {
            close(gZipInputStream);
        }
    }

    private ConnectionTools() {
        // empty
    }

}
