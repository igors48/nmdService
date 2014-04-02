package nmd.rss.collector.gae.persistence;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 20.03.2014
 */
public enum Kind {

    FEED_HEADER("FeedHeader"),
    FEED_ITEM("FeedItem"),
    FEED_UPDATE_TASK("FeedUpdateTask"),
    READ_FEED_ITEM("ReadFeedItem"),
    CATEGORY("Category");

    public final String value;

    private Kind(String value) {
        this.value = value;
    }

}
