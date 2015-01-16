package nmd.orb.http.responses;

import com.google.gson.Gson;
import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.payload.ExportReportPayload;
import nmd.orb.reader.Category;
import nmd.orb.services.report.ExportReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 25.11.2014.
 */
public class ExportReportResponse {

    private static final Gson GSON = new Gson();

    public List<ExportReportPayload> export;

    private ExportReportResponse() {
        // empty
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExportReportResponse that = (ExportReportResponse) o;

        if (!export.equals(that.export)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return export.hashCode();
    }

    public static ExportReportResponse create(final ExportReport report) {
        guard(notNull(report));

        final ExportReportResponse response = new ExportReportResponse();

        response.export = new ArrayList<>();

        for (final Category category : report.map.keySet()) {
            final Set<FeedHeader> headers = report.map.get(category);

            final ExportReportPayload payload = ExportReportPayload.create(category, headers);
            response.export.add(payload);
        }

        return response;
    }

    public static ExportReportResponse convert(final String string) {
        guard(notNull(string));

        return GSON.fromJson(string, ExportReportResponse.class);
    }

}
