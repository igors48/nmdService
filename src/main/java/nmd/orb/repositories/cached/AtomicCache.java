package nmd.orb.repositories.cached;

import com.google.appengine.api.memcache.MemcacheService;
import nmd.orb.repositories.Cache;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 25.09.2016.
 */
public class AtomicCache implements Cache {

    private final MemcacheService cache;

    public AtomicCache(MemcacheService cache) {
        guard(notNull(cache));
        this.cache = cache;
    }

    @Override
    public void put(Object key, Object object) {

    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public boolean delete(Object key) {
        return false;
    }

    @Override
    public void flush() {

    }
}
