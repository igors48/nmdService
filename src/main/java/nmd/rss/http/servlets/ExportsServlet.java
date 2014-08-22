package nmd.rss.http.servlets;

import nmd.rss.http.BaseServlet;

import static nmd.rss.http.servlets.exports.ExportsServletGetRequestHandler.EXPORTS_SERVLET_GET_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportsServlet extends BaseServlet {

    public ExportsServlet() {
        super();

        this.handlers.put(GET, EXPORTS_SERVLET_GET_REQUEST_HANDLER);
    }

}
