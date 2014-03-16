package unit.feed.controller.stub;

import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class UrlFetcherStub implements UrlFetcher {

    private String data;

    @Override
    public String fetch(final String link) throws UrlFetcherException {
        return this.data;
    }

    public void setData(final String data) {
        this.data = data;
    }

}
