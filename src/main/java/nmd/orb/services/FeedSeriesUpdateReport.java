package nmd.orb.services;

import nmd.orb.error.ServiceError;

import java.util.List;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * @author : igu
 */
public class FeedSeriesUpdateReport {

    public final List<FeedUpdateReport> updated;
    public final List<ServiceError> errors;

    public FeedSeriesUpdateReport(final List<FeedUpdateReport> updated, final List<ServiceError> errors) {
        assertNotNull(updated);
        this.updated = updated;

        assertNotNull(errors);
        this.errors = errors;
    }

}
