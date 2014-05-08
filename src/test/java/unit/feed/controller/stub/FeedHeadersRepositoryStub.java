package unit.feed.controller.stub;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.updater.FeedHeadersRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.06.13
 */
public class FeedHeadersRepositoryStub implements FeedHeadersRepository {

    private final List<FeedHeader> feedHeaders;

    public FeedHeadersRepositoryStub() {
        this.feedHeaders = new ArrayList<>();
    }

    @Override
    public FeedHeader loadHeader(final UUID feedId) {

        for (final FeedHeader candidate : this.feedHeaders) {

            if (feedId.equals(candidate.id)) {
                return candidate;
            }
        }

        return null;
    }

    @Override
    public List<FeedHeader> loadHeaders() {
        return new ArrayList<>(this.feedHeaders);
    }

    @Override
    public void deleteHeader(final UUID feedId) {

        for (final Iterator<FeedHeader> iterator = this.feedHeaders.iterator(); iterator.hasNext(); ) {
            final FeedHeader candidate = iterator.next();

            if (feedId.equals(candidate.id)) {
                iterator.remove();
            }
        }
    }

    @Override
    public FeedHeader loadHeader(final String feedLink) {

        for (final FeedHeader candidate : this.feedHeaders) {

            if (feedLink.equals(candidate.feedLink)) {
                return candidate;
            }
        }

        return null;
    }

    @Override
    public void storeHeader(final FeedHeader feedHeader) {
        deleteHeader(feedHeader.id);

        this.feedHeaders.add(feedHeader);
    }

    public boolean isEmpty() {
        return this.feedHeaders.isEmpty();
    }

}
