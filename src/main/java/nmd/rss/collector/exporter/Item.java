package nmd.rss.collector.exporter;

import java.util.Date;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.05.13
 */
class Item {

    private String description;
    private String link;
    private String title;
    private Date pubDate;

    public Item() {
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

    public Date getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(final Date pubDate) {
        assertNotNull(pubDate);
        this.pubDate = pubDate;
    }

}
