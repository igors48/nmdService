package nmd.orb.repositories.cached;

import nmd.orb.repositories.Cache;
import nmd.orb.repositories.UpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;

import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CachedUpdateErrorsRepository implements UpdateErrorsRepository {

    private final Cache cache;

    public CachedUpdateErrorsRepository(final Cache cache) {
        guard(notNull(cache));
        this.cache = cache;
    }

    @Override
    public void store(final UpdateErrors updateErrors) {
        guard(notNull(updateErrors));

        final String key = keyFor(updateErrors.feedId);

        this.cache.put(key, updateErrors);
    }

    @Override
    public UpdateErrors load(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        final String key = keyFor(feedId);

        return (UpdateErrors) this.cache.get(key);
    }

    @Override
    public void delete(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        final String key = keyFor(feedId);

        this.cache.delete(key);
    }

    private static String keyFor(final UUID feedId) {
        return "UpdateErrors-" + feedId;
    }

}
