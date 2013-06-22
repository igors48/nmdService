package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControlServiceException extends ServiceException {

    public ControlServiceException(final ServiceError error, final Throwable cause) {
        super(error, cause);
    }

    public ControlServiceException(final ServiceError error) {
        super(error);
    }

}
