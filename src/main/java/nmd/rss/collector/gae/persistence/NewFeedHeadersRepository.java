package nmd.rss.collector.gae.persistence;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.updater.FeedHeadersRepository;

import java.util.List;
import java.util.UUID;

/**
 * User: igu
 * Date: 15.10.13
 */
public class NewFeedHeadersRepository implements FeedHeadersRepository {

    @Override
    public FeedHeader loadHeader(UUID feedId) {
        return null;
    }

    @Override
    public List<FeedHeader> loadHeaders() {
        return null;
    }

    @Override
    public void deleteHeader(UUID feedId) {

    }

    @Override
    public FeedHeader loadHeader(String feedLink) {
        return null;
    }

    @Override
    public void storeHeader(FeedHeader feedHeader) {

    }

    @Override
    public List loadAllEntities() {
        return null;
    }

    @Override
    public void remove(Object victim) {

    }

}
