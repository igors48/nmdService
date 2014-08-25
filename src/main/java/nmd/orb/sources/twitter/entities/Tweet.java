package nmd.orb.sources.twitter.entities;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 26.02.14
 */
public class Tweet {

    private String id_str;
    private String created_at;
    private String text;
    private User user;
    private TweetEntities entities;

    Tweet() {
        this("", "", "", new User(), new TweetEntities());
    }

    public Tweet(final String id_str, final String created, final String text, final User user, final TweetEntities entities) {
        assertNotNull(id_str);
        this.id_str = id_str;

        assertNotNull(created);
        this.created_at = created;

        assertNotNull(text);
        this.text = text;

        assertNotNull(user);
        this.user = user;

        assertNotNull(entities);
        this.entities = entities;
    }

    public String getId_str() {
        return this.id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(final String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public TweetEntities getEntities() {
        return this.entities;
    }

    public void setEntities(final TweetEntities entities) {
        this.entities = entities;
    }

}
