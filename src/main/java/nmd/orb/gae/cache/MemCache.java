package nmd.orb.gae.cache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import nmd.orb.repositories.Cache;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.03.14
 */
public enum MemCache implements Cache {

    INSTANCE;

    private final MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    ;

    @Override
    public synchronized void put(final Object key, final Object object) {
        assertNotNull(key);
        assertNotNull(object);

        cache.put(key, object);
    }

    @Override
    public synchronized Object get(final Object key) {
        assertNotNull(key);

        return cache.get(key);
    }

    @Override
    public synchronized boolean delete(final Object key) {
        assertNotNull(key);

        return cache.delete(key);
    }

}
