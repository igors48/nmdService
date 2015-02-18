package nmd.orb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import static java.lang.String.format;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CloseableTools.close;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ConnectionTools {

    private static final Logger LOGGER = Logger.getLogger(ConnectionTools.class.getName());

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

        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");

        return connection;
    }

    public static String readAllFromConnection(final HttpURLConnection connection) throws IOException {
        guard(notNull(connection));

        final byte[] bytes = readAllBytesFromConnection(connection);

        return new String(bytes, UTF_8);
    }

    public static byte[] readAllBytesFromConnection(final HttpURLConnection connection) throws IOException {
        InputStream inputStream = null;

        try {
            connection.connect();

            inputStream = connection.getInputStream();

            final Map<String, List<String>> headers = connection.getHeaderFields();
            final boolean gZipped = ifGZipped(headers);

            if (gZipped) {
                LOGGER.info(format("GZipped content received from [ %s ]", connection.getURL().toString()));
            }

            return gZipped ? readFullyAsGZipped(inputStream) : readFullyAsUncompressed(inputStream);
        } finally {
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

            final byte[] uncompressed = buffer.toByteArray();

            final double ratio = 100d * compressed.length / uncompressed.length;
            LOGGER.info(format("Sizes compressed [ %d ] uncompressed [ %d ] compression [ %.2f%% ]", compressed.length, uncompressed.length, ratio));

            return uncompressed;
        } finally {
            close(gZipInputStream);
        }
    }

    private ConnectionTools() {
        // empty
    }

}
