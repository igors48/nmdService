package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.administration.AdministrationServletPostRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 29.11.13
 */
public class AdministrationServlet extends BaseServlet {

    public AdministrationServlet() {
        super();

        this.handlers.put(POST, new AdministrationServletPostRequestHandler(GaeWrappers.INSTANCE.getAdministrationServiceWrapper()));
    }

}
