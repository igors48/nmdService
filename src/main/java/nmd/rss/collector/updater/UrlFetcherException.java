package nmd.rss.collector.updater;

import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public class UrlFetcherException extends ServiceException {

    public UrlFetcherException(final ServiceError error, final Throwable cause) {
        super(error, cause);
    }

}
