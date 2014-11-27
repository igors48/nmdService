package nmd.orb.http.servlets.backup;

import com.google.gson.Gson;
import nmd.orb.http.Handler;
import nmd.orb.http.responses.BackupReportResponse;
import nmd.orb.http.tools.ResponseBody;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.invalidBackupFile;
import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 27.11.2014.
 */
public class BackupServletPostRequestHandler implements Handler {

    public static final BackupServletPostRequestHandler BACKUP_SERVLET_POST_REQUEST_HANDLER = new BackupServletPostRequestHandler();

    private static Gson GSON = new Gson();

    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        try {
            final BackupReportResponse backupReportResponse = GSON.fromJson(body, BackupReportResponse.class);

            return createJsonResponse(create("Backup file is uploaded"));
        } catch (Exception exception) {
            return createErrorJsonResponse(invalidBackupFile());
        }
    }

}
