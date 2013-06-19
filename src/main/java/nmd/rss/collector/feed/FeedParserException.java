package nmd.rss.collector.feed;

import com.sun.syndication.io.FeedException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedParserException extends Exception {

    public FeedParserException(final FeedException exception) {
        super(exception);
    }

}
