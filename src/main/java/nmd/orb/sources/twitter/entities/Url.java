package nmd.orb.sources.twitter.entities;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 26.02.14
 */
public class Url {

    private List<Urls> urls;

    Url() {
        this(new ArrayList<Urls>());
    }

    public Url(final List<Urls> urls) {
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
