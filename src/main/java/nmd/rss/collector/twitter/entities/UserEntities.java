package nmd.rss.collector.twitter.entities;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 26.02.14
 */
public class UserEntities {

    private Url url;

    UserEntities() {
        this(new Url());
    }

    public UserEntities(final Url url) {
        assertNotNull(url);
        this.url = url;
    }

    public void setUrl(final Url url) {
        this.url = url;
    }

    public Url getUrl() {
        return this.url;
    }

}
