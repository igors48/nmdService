package nmd.orb.error;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ServiceException extends Exception {

    protected final ServiceError error;

    public ServiceException(final ServiceError error, final Throwable cause) {
        super(cause);

        assertNotNull(error);
        this.error = error;
    }

    public ServiceException(final ServiceError error) {
        super();

        assertNotNull(error);
        this.error = error;
    }

    public ServiceError getError() {
        return this.error;
    }
}
