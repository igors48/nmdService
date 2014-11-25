package nmd.orb.http.responses;

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

    public List<BackupReportPayload> backup;

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

}
