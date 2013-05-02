package nmd.rss.collector.fetcher;

import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static nmd.rss.collector.util.Assert.assertValidUrl;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeUrlFetcher implements UrlFetcher {

    @Override
    public String fetch(final String link) throws UrlFetcherException {
        assertValidUrl(link);

        InputStreamReader urlStreamReader = null;
        InputStream urlStream = null;
        BufferedReader urlDataReader = null;

        try {
            URL url = new URL(link);

            urlStream = url.openStream();
            urlStreamReader = new InputStreamReader(urlStream);
            urlDataReader = new BufferedReader(urlStreamReader);

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = urlDataReader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception exception) {
            throw new UrlFetcherException(exception);
        } finally {
            close(urlDataReader);
            close(urlStream);
            close(urlStreamReader);
        }
    }

}
