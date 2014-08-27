package unit.feed.controller.stub;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class UrlFetcherStub implements UrlFetcher {

    private boolean simulateError;
    private String data;

    public UrlFetcherStub() {
        this.simulateError = false;
    }

    @Override
    public String fetch(final String link) throws UrlFetcherException {

        if (this.simulateError) {
            throw new UrlFetcherException(new NullPointerException());
        }

        return this.data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public void simulateError(boolean simulateError) {
        this.simulateError = simulateError;
    }

}
