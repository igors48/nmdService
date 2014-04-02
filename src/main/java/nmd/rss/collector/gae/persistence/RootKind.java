package nmd.rss.collector.gae.persistence;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 20.03.2014
 */
public enum RootKind {

    FEED("Feed"),
    CATEGORY("Category");

    public final String value;

    private RootKind(String value) {
        this.value = value;
    }

}
