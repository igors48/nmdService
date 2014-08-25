package nmd.orb.sources.twitter.entities;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 26.02.14
 */
public class Urls {

    private String url;
    private String expanded_url;

    Urls() {
        this("", "");
    }

    public Urls(final String url, final String expanded_url) {
        assertNotNull(url);
        this.url = url;

        assertNotNull(expanded_url);
        this.expanded_url = expanded_url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getExpanded_url() {
        return this.expanded_url;
    }

    public void setExpanded_url(final String expanded_url) {
        this.expanded_url = expanded_url;
    }

}
