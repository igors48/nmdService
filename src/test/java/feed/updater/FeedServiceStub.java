package feed.updater;

import nmd.rss.collector.feed.Feed;
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

    private FeedHeader header;
    private List<FeedItem> items;
    private List<FeedItem> removed;
    private List<FeedItem> retained;
    private List<FeedItem> added;

    public FeedServiceStub(final FeedHeader header, final List<FeedItem> items) {
        this.header = header;
        this.items = items;

        this.removed = null;
        this.retained = null;
        this.added = null;
    }

    @Override
    public UUID findForLink(final String link) throws FeedServiceException {
        return this.header == null ? null : (this.header.feedLink.equals(link) ? header.id : null);
    }

    @Override
    public UUID addFeed(final Feed feed) throws FeedServiceException {
        this.header = feed.header;
        this.items = feed.items;

        return this.header.id;
    }

    @Override
    public boolean removeFeed(final UUID feedId) throws FeedServiceException {

        if (this.header.id.equals(feedId)) {
            this.header = null;
            this.items = null;

            return true;
        } else {
            return false;
        }
    }

    @Override
    public FeedHeader loadHeader(final UUID feedId) throws FeedServiceException {
        return this.header;
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) throws FeedServiceException {
        return this.items;
    }

    @Override
    public void updateItems(final UUID feedId, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) throws FeedServiceException {
        this.removed = removed;
        this.retained = retained;
        this.added = added;
    }

    @Override
    public void clearAll() {
        // empty
    }

    public List<FeedItem> getRemoved() {
        return this.removed;
    }

    public List<FeedItem> getRetained() {
        return this.retained;
    }

    public List<FeedItem> getAdded() {
        return this.added;
    }

    public void setItems(final List<FeedItem> items) {
        this.items = items;
    }

    public void setHeader(final FeedHeader header) {
        this.header = header;
    }

}
