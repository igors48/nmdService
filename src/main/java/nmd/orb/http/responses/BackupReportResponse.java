package nmd.orb.http.responses;

import com.google.gson.Gson;
import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.payload.BackupReportPayload;
import nmd.orb.reader.Category;
import nmd.orb.services.report.BackupReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 25.11.2014.
 */
public class BackupReportResponse {

    private static final Gson GSON = new Gson();

    public List<BackupReportPayload> backup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BackupReportResponse that = (BackupReportResponse) o;

        if (!backup.equals(that.backup)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return backup.hashCode();
    }

    public static BackupReportResponse create(final BackupReport report) {
        guard(notNull(report));

        final BackupReportResponse response = new BackupReportResponse();

        response.backup = new ArrayList<>();

        for (final Category category : report.map.keySet()) {
            final Set<FeedHeader> headers = report.map.get(category);

            final BackupReportPayload payload = BackupReportPayload.create(category, headers);
            response.backup.add(payload);
        }

        return response;
    }

    public static BackupReportResponse convert(final String string) {
        guard(notNull(string));

        return GSON.fromJson(string, BackupReportResponse.class);
    }

}
