package nmd.orb.gae.fetcher;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");

            connection.connect();

            inputStream = connection.getInputStream();
            final Map<String, List<String>> map = connection.getHeaderFields();

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

        java.io.ByteArrayInputStream bytein = new java.io.ByteArrayInputStream(buffer.toByteArray());
        java.util.zip.GZIPInputStream gzin = new java.util.zip.GZIPInputStream(bytein);
        java.io.ByteArrayOutputStream byteout = new java.io.ByteArrayOutputStream();

        int res = 0;
        byte buf[] = new byte[1024];
        while (res >= 0) {
            res = gzin.read(buf, 0, buf.length);
            if (res > 0) {
                byteout.write(buf, 0, res);
            }
        }
        byte uncompressed[] = byteout.toByteArray();
        return byteout.toByteArray();
        //return buffer.toByteArray();
    }

    private GaeUrlFetcher() {
        // empty
    }

}
