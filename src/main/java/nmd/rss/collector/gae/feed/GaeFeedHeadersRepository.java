package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.gae.feed.header.FeedHeaderEntity;
import nmd.rss.collector.gae.feed.header.FeedHeaderHelper;
import nmd.rss.collector.updater.FeedHeadersRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        super(entityManager, FeedHeaderEntity.NAME);
    }

    @Override
    public FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedHeaderEntity> query = buildSelectWhereQuery("id", feedId.toString(), FeedHeaderEntity.class);

        return getFirstResultItemFrom(query);
    }

    @Override
    public void deleteHeader(final UUID feedId) {
        assertNotNull(feedId);

        final Query query = buildDeleteWhereQuery("id", feedId.toString());

        query.executeUpdate();
    }

    @Override
    public FeedHeader loadHeader(final String feedLink) {
        assertStringIsValid(feedLink);

        final TypedQuery<FeedHeaderEntity> query = buildSelectWhereQuery("feedLink", feedLink, FeedHeaderEntity.class);

        return getFirstResultItemFrom(query);
    }

    @Override
    public void storeHeader(final nmd.rss.collector.feed.FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        deleteHeader(feedHeader.id);

        final FeedHeaderHelper feedItemHelper = FeedHeaderHelper.convert(feedHeader);
        final String data = new Gson().toJson(feedItemHelper);

        final FeedHeaderEntity entity = new FeedHeaderEntity(feedHeader.id.toString(), feedHeader.feedLink, data);

        persist(entity);
    }

    private FeedHeader getFirstResultItemFrom(final TypedQuery<FeedHeaderEntity> query) {
        final List<FeedHeaderEntity> headers = query.getResultList();

        if (headers.isEmpty()) {
            return null;
        }

        final String data = headers.get(0).getData();

        return new Gson().fromJson(data, nmd.rss.collector.feed.FeedHeader.class);
    }

}
