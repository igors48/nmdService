package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;

import static nmd.rss.collector.rest.ControlServiceWrapper.clear;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends RestServlet {

    static {

        // POST -- clear service
        HANDLERS.put("POST", new Handler() {
            @Override
            public ResponseBody handle(final HttpServletRequest request) {
                return clear();
            }
        });
    }

}
