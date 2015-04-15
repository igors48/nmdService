package nmd.orb.sources.rss;

import com.sun.syndication.io.FeedException;
import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;

import java.io.UnsupportedEncodingException;

import static nmd.orb.error.ServiceError.feedParseError;
import static nmd.orb.sources.rss.FeedParser.parse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CharsetTools.detectCharset;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class RssClient {

    public static Feed fetchAsRssUrl(final String feedUrl, final UrlFetcher fetcher) throws UrlFetcherException, FeedException, ServiceException, UnsupportedEncodingException {
        guard(isValidUrl(feedUrl));
        guard(notNull(fetcher));

        try {
            byte[] bytes = fetcher.fetch(feedUrl);

            String originCharset = detectCharset(bytes);
            String string = new String(bytes, originCharset);

            return parse(feedUrl, string);
        } catch (RuntimeException exception) {
            throw new ServiceException(feedParseError(feedUrl));
        }
    }


    private RssClient() {
        // empty
    }

}
