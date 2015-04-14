package unit.feed.controller.stub;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.UnsupportedEncodingException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class UrlFetcherStub implements UrlFetcher {

    private static final String UTF_8 = "UTF-8";

    private boolean simulateError;
    private String data;

    public UrlFetcherStub() {
        this.simulateError = false;
    }

    @Override
    public byte[] fetch(final String link) throws UrlFetcherException {

        if (this.simulateError) {
            throw new UrlFetcherException(new NullPointerException());
        }

        try {
            return this.data.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new UrlFetcherException(new NullPointerException());
        }
    }

    @Override
    public String fetchString(String link) throws UrlFetcherException {
        throw new NotImplementedException();
    }

    public void setData(final String data) {
        this.data = data;
    }

    public void simulateError(boolean simulateError) {
        this.simulateError = simulateError;
    }

}
