package nmd.orb.sources.twitter.entities;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 26.02.14
 */
public class TweetEntities {

    private List<Urls> urls;

    TweetEntities() {
        this(new ArrayList<Urls>());
    }

    public TweetEntities(final List<Urls> urls) {
        assertNotNull(urls);
        this.urls = urls;
    }

    public void setUrls(final List<Urls> urls) {
        this.urls = urls;
    }

    public List<Urls> getUrls() {
        return this.urls;
    }

}
