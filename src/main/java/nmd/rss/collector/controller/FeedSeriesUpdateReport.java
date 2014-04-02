package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceError;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

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
