package nmd.orb.gae.cache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import nmd.orb.repositories.Cache;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.03.14
 */
public enum MemCache implements Cache {

    INSTANCE;

    private final MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

    @Override
    public synchronized void put(final Object key, final Object object) {
        guard(notNull(key));
        guard(notNull(object));

        cache.put(key, object);
    }

    @Override
    public synchronized Object get(final Object key) {
        guard(notNull(key));

        return cache.get(key);
    }

    @Override
    public synchronized boolean delete(final Object key) {
        guard(notNull(key));

        return cache.delete(key);
    }

    @Override
    public void clear() {
        cache.clearAll();
    }

}
