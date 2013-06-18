package nmd.rss.collector.error;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.06.13
 */
public class ServiceException extends Exception {

    private final ServiceError error;

    public ServiceException(final ServiceError error, final Throwable cause) {
        super(cause);

        assertNotNull(error);
        this.error = error;
    }

    public ServiceError getError() {
        return this.error;
    }

}
