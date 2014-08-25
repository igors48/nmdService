package nmd.orb.collector.exporter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static nmd.orb.collector.util.Assert.assertNotNull;
import static nmd.orb.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.05.13
 */
@XmlRootElement
class Channel {

    private String description;
    private String title;
    private String link;
    @XmlElement(name = "item")
    private List<Item> items;

    Channel() {
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

    List<Item> getItems() {
        return this.items;
    }

    void setItems(final List<Item> items) {
        assertNotNull(items);
        this.items = items;
    }

}
