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

    public static final ReadsServletGetRequestHandler READS_SERVLET_GET_REQUEST_HANDLER = new ReadsServletGetRequestHandler(GaeWrappers.INSTANCE.getReadsServiceWrapper());
    public static final ReadsServletPutRequestHandler READS_SERVLET_PUT_REQUEST_HANDLER = new ReadsServletPutRequestHandler(GaeWrappers.INSTANCE.getReadsServiceWrapper());


    public ReadsServlet() {
        super();

        this.handlers.put(GET, READS_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(PUT, READS_SERVLET_PUT_REQUEST_HANDLER);
    }

}
