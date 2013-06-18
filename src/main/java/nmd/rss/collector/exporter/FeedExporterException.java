package nmd.rss.collector.exporter;

import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 17.05.13
 */
public class FeedExporterException extends ServiceException {

    public FeedExporterException(final ServiceError error, final Throwable cause) {
        super(error, cause);
    }

}
