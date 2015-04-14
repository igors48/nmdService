package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;

/**
 * @author : igu
 */
public interface ContentFilterServiceWrapper {

    ResponseBody filter(String link);

}
