package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.reset.ResetServletPostRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ResetServlet extends BaseServlet {

    public static final ResetServletPostRequestHandler RESET_SERVLET_POST_REQUEST_HANDLER = new ResetServletPostRequestHandler(GaeWrappers.INSTANCE.getResetServiceWrapper());

    public ResetServlet() {
        super();

        this.handlers.put(POST, RESET_SERVLET_POST_REQUEST_HANDLER);
    }

}
