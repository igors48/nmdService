package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.cron.CronServletGetRequestHandler;

/**
 * Created by igor on 09.12.2014.
 */
public class CronServlet extends BaseServlet {

    public CronServlet() {
        super();

        this.handlers.put(GET, new CronServletGetRequestHandler(GaeWrappers.INSTANCE.getCronServiceWrapper()));
    }

}
