package nmd.rss.collector.feed;

import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class FeedParserException extends ServiceException {

    public FeedParserException(final ServiceError error, final Throwable cause) {
        super(error, cause);
    }

}
