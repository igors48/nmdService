package nmd.orb.sources.twitter.entities;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 26.02.14
 */
public class User {

    private String name;
    private String screen_name;
    private String description;
    private UserEntities entities;

    User() {
        this("", "", "", new UserEntities());
    }

    public User(final String name, final String screen_name, final String description, final UserEntities entities) {
        assertNotNull(name);
        this.name = name;

        assertNotNull(screen_name);
        this.screen_name = screen_name;

        assertNotNull(description);
        this.description = description;

        assertNotNull(entities);
        this.entities = entities;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return this.screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public UserEntities getEntities() {
        return this.entities;
    }

    public void setEntities(final UserEntities entities) {
        this.entities = entities;
    }

}
