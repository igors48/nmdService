package nmd.orb.gae.fetcher;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.util.Assert.assertValidUrl;
import static nmd.orb.util.CloseableTools.close;
import static nmd.orb.util.ConnectionTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeUrlFetcher implements UrlFetcher {

    private static final Logger LOGGER = Logger.getLogger(GaeUrlFetcher.class.getName());

    public static final UrlFetcher GAE_URL_FETCHER = new GaeUrlFetcher();

    @Override
    public byte[] fetch(final String link) throws UrlFetcherException {
        assertValidUrl(link);

        InputStream inputStream = null;

        try {
            final URL url = new URL(link);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");

            connection.connect();

            inputStream = connection.getInputStream();

            final Map<String, List<String>> headers = connection.getHeaderFields();
            final boolean gZipped = ifGZipped(headers);

            if (gZipped) {
                LOGGER.info(format("GZipped content received from [ %s ]", link));
            }

            return gZipped ? readFullyAsGZipped(inputStream) : readFullyAsUncompressed(inputStream);
        } catch (Exception exception) {
            throw new UrlFetcherException(exception);
        } finally {
            close(inputStream);
        }
    }

    private GaeUrlFetcher() {
        // empty
    }

}
