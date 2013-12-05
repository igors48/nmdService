package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;

/**
 * User: igu
 * Date: 05.12.13
 */
public interface Handler {

    ResponseBody handle(HttpServletRequest request) throws Exception;

}
