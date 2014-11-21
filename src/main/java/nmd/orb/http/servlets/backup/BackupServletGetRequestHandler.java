package nmd.orb.http.servlets.backup;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ContentType;
import nmd.orb.http.tools.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author : igu
 */
public class BackupServletGetRequestHandler implements Handler {

    public static final BackupServletGetRequestHandler BACKUP_SERVLET_GET_REQUEST_HANDLER = new BackupServletGetRequestHandler();

    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        return new ResponseBody(ContentType.JSON, "['sdf']", "sdf.json");
    }

}
