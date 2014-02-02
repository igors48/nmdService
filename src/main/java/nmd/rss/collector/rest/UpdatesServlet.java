package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.parseFeedId;
import static nmd.rss.collector.rest.ServletTools.pathInfoIsEmpty;
import static nmd.rss.collector.rest.UpdatesServiceWrapper.updateCurrentFeed;
import static nmd.rss.collector.rest.UpdatesServiceWrapper.updateFeed;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class UpdatesServlet extends AbstractRestServlet {

    // GET -- update current feed
    // GET /{feedId} -- update feed
    @Override
    protected ResponseBody handleGet(HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        if (pathInfoIsEmpty(pathInfo)) {
            return updateCurrentFeed();
        }

        final UUID feedId = parseFeedId(pathInfo);

        return feedId == null ? createErrorJsonResponse(invalidFeedId(pathInfo)) : updateFeed(feedId);
    }

}
