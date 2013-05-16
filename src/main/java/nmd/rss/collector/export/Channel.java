package nmd.rss.collector.export;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.05.13
 */
class Channel {

    private String description;
    private String title;
    private String link;

    private Channel() {
        // empty
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        assertStringIsValid(description);
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        assertStringIsValid(title);
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String link) {
        assertStringIsValid(link);
        this.link = link;
    }

}
