package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.backup.BackupServletGetRequestHandler.BACKUP_SERVLET_GET_REQUEST_HANDLER;
import static nmd.orb.http.servlets.backup.BackupServletPostRequestHandler.BACKUP_SERVLET_POST_REQUEST_HANDLER;

/**
 * @author : igu
 */
public class BackupServlet extends BaseServlet {

    public BackupServlet() {
        super();

        this.handlers.put(GET, BACKUP_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, BACKUP_SERVLET_POST_REQUEST_HANDLER);
    }

}
