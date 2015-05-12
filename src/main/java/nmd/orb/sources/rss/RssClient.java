package nmd.orb.sources.rss;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;

import static nmd.orb.error.ServiceError.feedParseError;
import static nmd.orb.error.ServiceError.urlFetcherError;
import static nmd.orb.sources.rss.FeedParser.parse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CharsetTools.detectCharset;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class RssClient {

    public static Feed fetchAsRssUrl(final String feedUrl, final UrlFetcher fetcher) throws ServiceException {
        guard(isValidUrl(feedUrl));
        guard(notNull(fetcher));

        try {
            final byte[] bytes = fetcher.fetch(feedUrl);

            final String originCharset = detectCharset(bytes);
            final String string = new String(bytes, originCharset);

            return parse(feedUrl, string);
        } catch (UrlFetcherException exception) {
            throw new ServiceException(urlFetcherError(feedUrl), exception);
        } catch (Exception exception) {
            throw new ServiceException(feedParseError(feedUrl), exception);
        }
    }


    private RssClient() {
        // empty
    }

}
