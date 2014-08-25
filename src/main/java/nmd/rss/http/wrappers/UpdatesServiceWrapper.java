package nmd.rss.http.wrappers;

import nmd.rss.http.tools.ResponseBody;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public interface UpdatesServiceWrapper {

    ResponseBody updateCurrentFeeds();

    ResponseBody updateFeed(UUID feedId);

}