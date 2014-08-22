package nmd.rss.http.servlets;

import nmd.rss.http.BaseServlet;

import static nmd.rss.http.servlets.updates.UpdatesServletGetRequestHandler.UPDATES_SERVLET_GET_REQUEST_HANDLER;

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
