package nmd.rss.collector.gae.fetcher;

import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static nmd.rss.collector.util.Assert.assertValidUrl;
import static nmd.rss.collector.util.CharsetTools.convertToUtf8;
import static nmd.rss.collector.util.CharsetTools.detectCharSet;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeUrlFetcher implements UrlFetcher {

    public static final UrlFetcher GAE_URL_FETCHER = new GaeUrlFetcher();

    private static final String UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE = "content-type";

    @Override
    public String fetch(final String link) throws UrlFetcherException {
        assertValidUrl(link);

        InputStreamReader urlStreamReader = null;
        InputStream urlStream = null;
        BufferedReader urlDataReader = null;

        try {
            final URL url = new URL(link);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            final String contentType = connection.getHeaderField(CONTENT_TYPE);
            String charset = detectCharSet(contentType);
            charset = charset == null ? UTF_8 : charset;

            urlStream = connection.getInputStream();
            urlStreamReader = new InputStreamReader(urlStream, charset);
            urlDataReader = new BufferedReader(urlStreamReader);

            final StringBuilder result = new StringBuilder();
            String line;

            while ((line = urlDataReader.readLine()) != null) {
                result.append(line);
            }

            return convertToUtf8(result.toString());
        } catch (Exception exception) {
            throw new UrlFetcherException(exception);
        } finally {
            close(urlDataReader);
            close(urlStream);
            close(urlStreamReader);
        }
    }

    private GaeUrlFetcher() {
        // empty
    }

}
