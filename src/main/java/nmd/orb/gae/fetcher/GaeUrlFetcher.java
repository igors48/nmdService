package nmd.orb.gae.fetcher;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static nmd.orb.util.Assert.assertValidUrl;
import static nmd.orb.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeUrlFetcher implements UrlFetcher {

    public static final UrlFetcher GAE_URL_FETCHER = new GaeUrlFetcher();

    @Override
    public byte[] fetch(final String link) throws UrlFetcherException {
        assertValidUrl(link);

        InputStream inputStream = null;

        try {
            final URL url = new URL(link);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();

            return readFully(inputStream);
        } catch (Exception exception) {
            throw new UrlFetcherException(exception);
        } finally {
            close(inputStream);
        }
    }

    private static byte[] readFully(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[16384];

        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private GaeUrlFetcher() {
        // empty
    }

}
