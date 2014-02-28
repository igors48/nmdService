package nmd.rss.collector.twitter;

/**
 * User: igu
 * Date: 26.02.14
 */
public class Tweet {

    private String created_at;
    private String text;
    private User user;
    private TweetEntities entities;

    public Tweet() {
        this.created_at = "";
        this.text = "";
        this.user = new User();
        this.entities = new TweetEntities();
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public String getText() {
        return this.text;
    }

    public User getUser() {
        return this.user;
    }

    public TweetEntities getEntities() {
        return this.entities;
    }

}
