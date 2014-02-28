package nmd.rss.collector.twitter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: igu
 * Date: 26.02.14
 */
public class TweetEntities {

    private List<Urls> urls;

    public TweetEntities() {
        this.urls = new ArrayList<Urls>();
    }

    public List<Urls> getUrls() {
        return this.urls;
    }

}
