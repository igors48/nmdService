package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.clear.ResetServletPostRequestHandler.RESET_SERVLET_POST_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ResetServlet extends BaseServlet {

    public ResetServlet() {
        super();

        this.handlers.put(POST, RESET_SERVLET_POST_REQUEST_HANDLER);
    }

}
