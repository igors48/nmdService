package nmd.orb.http.servlets.backup;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;

import java.util.List;
import java.util.Map;

import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 27.11.2014.
 */
public class BackupServletPostRequestHandler implements Handler {

    public static final BackupServletPostRequestHandler BACKUP_SERVLET_POST_REQUEST_HANDLER = new BackupServletPostRequestHandler();

    @Override
    public ResponseBody handle(List<String> elements, Map<String, String> parameters, String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return createJsonResponse(create("ok"));
    }

}
