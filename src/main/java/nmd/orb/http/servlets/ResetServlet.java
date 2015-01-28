package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.reset.ResetServletPostRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class ResetServlet extends BaseServlet {

    public ResetServlet() {
        super();

        this.handlers.put(POST, new ResetServletPostRequestHandler(GaeWrappers.INSTANCE.getResetServiceWrapper()));
    }

}
