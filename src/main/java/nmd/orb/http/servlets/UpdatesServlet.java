package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.updates.UpdatesServletGetRequestHandler.UPDATES_SERVLET_GET_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class UpdatesServlet extends BaseServlet {

    public UpdatesServlet() {
        super();

        this.handlers.put(GET, UPDATES_SERVLET_GET_REQUEST_HANDLER);
    }

}
