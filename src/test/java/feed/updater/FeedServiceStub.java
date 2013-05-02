package feed.updater;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.FeedServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class FeedServiceStub implements FeedService {

    @Override
    public FeedHeader loadHeader(UUID feedId) throws FeedServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<FeedItem> loadItems(UUID feedId) throws FeedServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateItems(UUID feedId, List<FeedItem> removed, List<FeedItem> added) throws FeedServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
