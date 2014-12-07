package nmd.orb.http.servlets.importer;

import nmd.orb.http.Handler;
import nmd.orb.http.responses.BackupReportResponse;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import nmd.orb.http.wrappers.ImportServiceWrapperImpl;
import nmd.orb.services.importer.ImportJobContext;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.invalidBackupFile;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 27.11.2014.
 */
public class ImportServletPostRequestHandler implements Handler {

    public static final ImportServletPostRequestHandler IMPORT_SERVLET_POST_REQUEST_HANDLER = new ImportServletPostRequestHandler(ImportServiceWrapperImpl.IMPORT_SERVICE_WRAPPER);

    private static final int DEFAULT_TRIES_COUNT = 5;

    private final ImportServiceWrapper importServiceWrapper;

    public ImportServletPostRequestHandler(final ImportServiceWrapper importServiceWrapper) {
        guard(notNull(importServiceWrapper));
        this.importServiceWrapper = importServiceWrapper;
    }

    //POST -- schedule import job
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        try {
            final BackupReportResponse backupReportResponse = BackupReportResponse.convert(body);
            final ImportJobContext context = ImportJobContext.convert(backupReportResponse.backup, DEFAULT_TRIES_COUNT);

            return this.importServiceWrapper.schedule(context);
        } catch (Exception exception) {
            return createErrorJsonResponse(invalidBackupFile());
        }
    }

}
