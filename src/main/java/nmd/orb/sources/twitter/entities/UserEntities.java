package nmd.orb.sources.twitter.entities;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
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
