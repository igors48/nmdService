package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.updates.UpdatesServletGetRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class UpdatesServlet extends BaseServlet {

    public static final UpdatesServletGetRequestHandler UPDATES_SERVLET_GET_REQUEST_HANDLER = new UpdatesServletGetRequestHandler(GaeWrappers.INSTANCE.getUpdatesServiceWrapper());

    public UpdatesServlet() {
        super();

        this.handlers.put(GET, UPDATES_SERVLET_GET_REQUEST_HANDLER);
    }

}
