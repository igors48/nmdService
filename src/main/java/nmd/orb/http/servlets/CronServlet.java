package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.cron.CronServletGetRequestHandler.CRON_SERVLET_GET_REQUEST_HANDLER;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServlet extends BaseServlet {

    public CronServlet() {
        super();

        this.handlers.put(GET, CRON_SERVLET_GET_REQUEST_HANDLER);
    }

}
