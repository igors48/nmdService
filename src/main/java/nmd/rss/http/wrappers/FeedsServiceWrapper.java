package nmd.rss.http.wrappers;

import nmd.rss.http.tools.ResponseBody;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.08.2014
 */
public interface FeedsServiceWrapper {

    ResponseBody addFeed(String feedUrl, String categoryId);

    ResponseBody updateFeedTitle(UUID feedId, String title);

    ResponseBody removeFeed(UUID feedId);

    ResponseBody getFeedHeaders();

    ResponseBody getFeedHeader(UUID feedId);

    ResponseBody getFeed(UUID feedId);

    ResponseBody clear();

}
