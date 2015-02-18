package nmd.orb.gae.fetcher;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;

import java.net.HttpURLConnection;

import static nmd.orb.util.Assert.assertValidUrl;
import static nmd.orb.util.ConnectionTools.Method.GET;
import static nmd.orb.util.ConnectionTools.readAllBytesFromConnection;
import static nmd.orb.util.ConnectionTools.setupConnection;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeUrlFetcher implements UrlFetcher {

    public static final UrlFetcher GAE_URL_FETCHER = new GaeUrlFetcher();

    @Override
    public byte[] fetch(final String link) throws UrlFetcherException {
        assertValidUrl(link);

        try {
            final HttpURLConnection connection = setupConnection(link, GET);
            return readAllBytesFromConnection(connection);
        } catch (Exception exception) {
            throw new UrlFetcherException(exception);
        }
    }

    private GaeUrlFetcher() {
        // empty
    }

}
