package feed.updater;

import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class UrlFetcherStub implements UrlFetcher {

    @Override
    public String fetch(String link) throws UrlFetcherException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
