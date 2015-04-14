package nmd.orb.util;

import nmd.orb.error.ServiceError;

/**
 * Created by igor on 12.02.2015.
 */
public class IllegalParameterException extends RuntimeException {

    public final ServiceError serviceError;

    public IllegalParameterException(final ServiceError serviceError) {
        super();
        this.serviceError = serviceError;
    }

}
