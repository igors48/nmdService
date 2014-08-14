package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.BaseServlet;

import static nmd.rss.collector.rest.servlets.reads.ReadsServletGetRequestHandler.READS_SERVLET_GET_REQUEST_HANDLER;
import static nmd.rss.collector.rest.servlets.reads.ReadsServletPutRequestHandler.READS_SERVLET_PUT_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class ReadsServlet extends BaseServlet {

    public ReadsServlet() {
        super();

        this.handlers.put(GET, READS_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(PUT, READS_SERVLET_PUT_REQUEST_HANDLER);
    }

}
