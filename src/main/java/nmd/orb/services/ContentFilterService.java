package nmd.orb.services;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.error.ServiceException;
import nmd.orb.services.content.BestNodeLocator;
import nmd.orb.services.content.SimpleBestNodeLocator;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import static nmd.orb.error.ServiceError.contentFilterError;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.CharsetTools.detectCharset;
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

    public String filter(final String link) throws ServiceException {
        guard(isValidUrl(link));

        try {
            final byte[] bytes = this.urlFetcher.fetch(link);
            final String originCharset = detectCharset(bytes);
            final String source = new String(bytes, originCharset);

            return filterContent(source);
        } catch (Exception exception) {
            throw new ServiceException(contentFilterError(link), exception);
        }
    }

    public static String filterContent(final String content) {
        guard(notNull(content));

        final HtmlCleaner htmlCleaner = new HtmlCleaner();
        final TagNode rootNode = htmlCleaner.clean(content);

        final BestNodeLocator bestNodeLocator = new SimpleBestNodeLocator();
        final TagNode bestNode = bestNodeLocator.findBest(rootNode);

        return htmlCleaner.getInnerHtml(bestNode);
    }

}
