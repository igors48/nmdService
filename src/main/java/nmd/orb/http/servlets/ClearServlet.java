package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.clear.ClearServletPostRequestHandler.CLEAR_SERVLET_POST_REQUEST_HANDLER;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends BaseServlet {

    public ClearServlet() {
        super();

        this.handlers.put(POST, CLEAR_SERVLET_POST_REQUEST_HANDLER);
    }

}
