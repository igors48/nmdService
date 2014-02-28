package nmd.rss.collector.twitter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: igu
 * Date: 26.02.14
 */
public class Url {

    private List<Urls> urls;

    public Url() {
        this.urls = new ArrayList<Urls>();
    }

    public List<Urls> getUrls() {
        return this.urls;
    }

}
