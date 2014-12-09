package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public interface UpdatesServiceWrapper {

    ResponseBody updateFeed(UUID feedId);

}
