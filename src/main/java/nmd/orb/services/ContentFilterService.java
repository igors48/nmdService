package nmd.orb.services;

import nmd.orb.collector.fetcher.UrlFetcher;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ContentFilterService {

    private final UrlFetcher urlFetcher;

    public ContentFilterService(final UrlFetcher urlFetcher) {
        guard(notNull(urlFetcher));
        this.urlFetcher = urlFetcher;
    }

    public String filter(final String link) {
        guard(isValidUrl(link));

        return "";
    }

}
