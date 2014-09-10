package nmd.orb.gae.cache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import nmd.orb.repositories.Cache;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.03.14
 */
public class MemCache implements Cache {

    public static final MemCache MEM_CACHE = new MemCache();

    private static final MemcacheService CACHE = MemcacheServiceFactory.getMemcacheService();

    @Override
    public synchronized void put(final Object key, final Object object) {
        assertNotNull(key);
        assertNotNull(object);

        CACHE.put(key, object);
    }

    @Override
    public synchronized Object get(final Object key) {
        assertNotNull(key);

        return CACHE.get(key);
    }

    @Override
    public synchronized boolean delete(final Object key) {
        assertNotNull(key);

        return CACHE.delete(key);
    }

    private MemCache() {
        // empty
    }
}
