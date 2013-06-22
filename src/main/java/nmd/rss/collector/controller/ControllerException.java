package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceError;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControllerException extends Exception {

    private final ServiceError error;

    public ControllerException(final ServiceError error, final Throwable cause) {
        super(cause);

        assertNotNull(error);
        this.error = error;
    }

    public ControllerException(final ServiceError error) {
        super();

        assertNotNull(error);
        this.error = error;
    }

    public ServiceError getError() {
        return this.error;
    }

}
