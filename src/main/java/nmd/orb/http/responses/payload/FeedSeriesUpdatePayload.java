package nmd.orb.http.responses.payload;

import nmd.orb.error.ServiceError;
import nmd.orb.services.report.FeedSeriesUpdateReport;
import nmd.orb.services.report.FeedUpdateReport;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedSeriesUpdatePayload {

    public List<FeedMergeReportPayload> updates;
    public List<ErrorPayload> errors;

    private FeedSeriesUpdatePayload() {
        // empty
    }

    public static FeedSeriesUpdatePayload convert(final FeedSeriesUpdateReport report) {
        guard(notNull(report));

        final List<FeedMergeReportPayload> updates = new ArrayList<>();

        for (final FeedUpdateReport update : report.updated) {
            final FeedMergeReportPayload helper = FeedMergeReportPayload.create(update);

            updates.add(helper);
        }

        final List<ErrorPayload> errors = new ArrayList<>();

        for (final ServiceError error : report.errors) {
            final ErrorPayload helper = ErrorPayload.create(error);

            errors.add(helper);
        }

        final FeedSeriesUpdatePayload payload = new FeedSeriesUpdatePayload();

        payload.updates = updates;
        payload.errors = errors;

        return payload;
    }

}
