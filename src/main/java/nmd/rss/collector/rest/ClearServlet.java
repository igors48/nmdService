package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;

import static nmd.rss.collector.rest.FeedsServiceWrapper.clear;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends AbstractRestServlet {

    // POST -- clear service
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        return clear();
    }

}
