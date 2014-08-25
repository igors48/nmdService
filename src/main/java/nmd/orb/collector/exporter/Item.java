package nmd.orb.collector.exporter;

import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.05.13
 */
class Item {

    private String description;
    private String link;
    private String guid;
    private String title;
    private String pubDate;

    Item() {
        // empty
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        assertStringIsValid(description);
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String link) {
        assertStringIsValid(link);
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        assertStringIsValid(title);
        this.title = title;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(final String pubDate) {
        assertNotNull(pubDate);
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(final String guid) {
        assertStringIsValid(guid);
        this.guid = guid;
    }

}
