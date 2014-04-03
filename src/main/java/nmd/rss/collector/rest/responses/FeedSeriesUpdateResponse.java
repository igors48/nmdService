package nmd.rss.collector.rest.responses;

import nmd.rss.collector.controller.FeedSeriesUpdateReport;
import nmd.rss.collector.controller.FeedUpdateReport;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.rest.responses.payload.ErrorPayload;
import nmd.rss.collector.rest.responses.payload.FeedMergeReportPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 21.03.2014
 */
public class FeedSeriesUpdateResponse extends SuccessResponse {

    public List<FeedMergeReportPayload> updates;
    public List<ErrorPayload> errors;

    private FeedSeriesUpdateResponse() {
        // empty
    }

    public static FeedSeriesUpdateResponse create(final List<FeedMergeReportPayload> updates, final List<ErrorPayload> errors) {
        guard(notNull(updates));
        guard(notNull(errors));

        final FeedSeriesUpdateResponse response = new FeedSeriesUpdateResponse();

        response.updates = updates;
        response.errors = errors;

        return response;
    }

    public static FeedSeriesUpdateResponse convert(final FeedSeriesUpdateReport report) {
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

        return create(updates, errors);
    }

}
