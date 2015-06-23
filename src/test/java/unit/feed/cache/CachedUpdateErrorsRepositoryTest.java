package unit.feed.cache;

import nmd.orb.error.ServiceError;
import nmd.orb.repositories.cached.CachedUpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class CachedUpdateErrorsRepositoryTest {

    private static final UpdateErrors FIRST_UPDATE_ERRORS = new UpdateErrors(UUID.randomUUID()).appendError(ServiceError.feedParseError("http:\\domain.com"));
    private static final UpdateErrors SECOND_UPDATE_ERRORS = new UpdateErrors(UUID.randomUUID()).appendError(ServiceError.feedParseError("http:\\domain.com"));

    private CacheStub cacheStub;
    private CachedUpdateErrorsRepository cachedUpdateErrorsRepository;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cachedUpdateErrorsRepository = new CachedUpdateErrorsRepository(this.cacheStub);
    }

    @Test
    public void whenErrorsStoredThenItIsReturnedByKey() {
        this.cachedUpdateErrorsRepository.store(FIRST_UPDATE_ERRORS);
        this.cachedUpdateErrorsRepository.store(SECOND_UPDATE_ERRORS);

        assertEquals(FIRST_UPDATE_ERRORS, this.cachedUpdateErrorsRepository.load(FIRST_UPDATE_ERRORS.feedId));
        assertEquals(SECOND_UPDATE_ERRORS, this.cachedUpdateErrorsRepository.load(SECOND_UPDATE_ERRORS.feedId));
    }

    @Test
    public void whenErrorsWithGivenKeyThenNullReturns() {
        assertNull(this.cachedUpdateErrorsRepository.load(UUID.randomUUID()));
    }

    @Test
    public void whenErrorsDeletedThenItsIsNotExistsAnyMore() {
        this.cachedUpdateErrorsRepository.store(FIRST_UPDATE_ERRORS);

        this.cachedUpdateErrorsRepository.delete(FIRST_UPDATE_ERRORS.feedId);

        assertNull(this.cachedUpdateErrorsRepository.load(FIRST_UPDATE_ERRORS.feedId));
    }

    @Test
    public void whenErrorsDeletedThenItsIsDeletedFromCache() {
        this.cachedUpdateErrorsRepository.store(FIRST_UPDATE_ERRORS);
        this.cachedUpdateErrorsRepository.store(SECOND_UPDATE_ERRORS);

        this.cachedUpdateErrorsRepository.delete(FIRST_UPDATE_ERRORS.feedId);
        this.cachedUpdateErrorsRepository.delete(SECOND_UPDATE_ERRORS.feedId);

        assertTrue(this.cacheStub.isEmpty());
    }

}
