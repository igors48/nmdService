package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.gae.feed.header.FeedHeader;
import nmd.rss.collector.gae.feed.header.FeedHeaderHelper;
import nmd.rss.collector.updater.FeedHeadersRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class GaeFeedHeadersRepository extends AbstractGaeRepository implements FeedHeadersRepository {

    public GaeFeedHeadersRepository(final EntityManager entityManager) {
        super(entityManager, FeedHeader.class.getSimpleName());
    }

    @Override
    public nmd.rss.collector.feed.FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedHeader> query = buildSelectWhereQuery("id", "id", FeedHeader.class);
        query.setParameter("id", feedId.toString());

        return getFirstResultItemFrom(query);
    }

    @Override
    public void deleteHeader(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedHeader> query = buildDeleteWhereQuery("id", "id", FeedHeader.class);
        query.setParameter("id", feedId.toString());

        query.executeUpdate();
    }

    @Override
    public nmd.rss.collector.feed.FeedHeader loadHeader(final String feedLink) {
        assertStringIsValid(feedLink);

        final TypedQuery<FeedHeader> query = buildSelectWhereQuery("feedLink", "feedLink", FeedHeader.class);
        query.setParameter("feedLink", feedLink);

        return getFirstResultItemFrom(query);
    }

    @Override
    public void storeHeader(final nmd.rss.collector.feed.FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        deleteHeader(feedHeader.id);

        final FeedHeaderHelper feedItemHelper = FeedHeaderHelper.convert(feedHeader);
        final String data = new Gson().toJson(feedItemHelper);

        final FeedHeader entity = new FeedHeader(feedHeader.id.toString(), feedHeader.feedLink, data);

        persist(entity);
    }

    private nmd.rss.collector.feed.FeedHeader getFirstResultItemFrom(final TypedQuery<FeedHeader> query) {
        final List<FeedHeader> headers = query.getResultList();

        if (headers.isEmpty()) {
            return null;
        }

        final String data = headers.get(0).getData();

        return new Gson().fromJson(data, nmd.rss.collector.feed.FeedHeader.class);
    }

}
