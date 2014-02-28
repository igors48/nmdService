package nmd.rss.collector.twitter;

/**
 * User: igu
 * Date: 26.02.14
 */
public class User {

    private String name;
    private String description;
    private UserEntities entities;

    public User() {
        this.name = "";
        this.description = "";
        this.entities = new UserEntities();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public UserEntities getEntities() {
        return this.entities;
    }

}
