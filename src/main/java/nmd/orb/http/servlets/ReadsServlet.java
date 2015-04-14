package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.reads.ReadsServletGetRequestHandler;
import nmd.orb.http.servlets.reads.ReadsServletPutRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends BaseServlet {

    public ReadsServlet() {
        super();

        this.handlers.put(GET, new ReadsServletGetRequestHandler(GaeWrappers.INSTANCE.getReadsServiceWrapper()));
        this.handlers.put(PUT, new ReadsServletPutRequestHandler(GaeWrappers.INSTANCE.getReadsServiceWrapper()));
    }

}
