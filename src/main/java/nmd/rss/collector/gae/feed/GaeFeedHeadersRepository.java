package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import nmd.rss.collector.gae.feed.header.FeedHeader;
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
public class GaeFeedHeadersRepository implements FeedHeadersRepository {

    private final EntityManager entityManager;

    public GaeFeedHeadersRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public nmd.rss.collector.feed.FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedHeader> query = this.entityManager.createQuery("SELECT feedHeader FROM FeedHeader feedHeader WHERE feedHeader.id = :id", FeedHeader.class);
        query.setParameter("id", feedId.toString());

        return getFirstResultItemFrom(query);
    }

    @Override
    public nmd.rss.collector.feed.FeedHeader loadHeader(final String feedLink) {
        assertStringIsValid(feedLink);

        final TypedQuery<FeedHeader> query = this.entityManager.createQuery("SELECT feedHeader FROM FeedHeader feedHeader WHERE feedHeader.feedLink = :feedLink", FeedHeader.class);
        query.setParameter("feedLink", feedLink);

        return getFirstResultItemFrom(query);
    }

    @Override
    public void storeHeader(final nmd.rss.collector.feed.FeedHeader feedHeader) {
        assertNotNull(feedHeader);

        final Query query = this.entityManager.createQuery("DELETE FROM FeedHeader feedHeader WHERE feedHeader.id = :id");
        query.setParameter("id", feedHeader.id.toString());
        query.executeUpdate();

        final FeedHeaderHelper feedItemHelper = FeedHeaderHelper.convert(feedHeader);
        final String data = new Gson().toJson(feedItemHelper);

        final FeedHeader entity = new FeedHeader(feedHeader.id.toString(), feedHeader.feedLink, data);

        this.entityManager.persist(entity);
    }

    @Override
    public List loadAllEntities() {
        final Query query = this.entityManager.createQuery("SELECT entity FROM FeedHeader entity");

        return query.getResultList();
    }

    @Override
    public void removeEntity(final Object victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
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
