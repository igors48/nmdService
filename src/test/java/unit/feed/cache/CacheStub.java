package unit.feed.cache;

import nmd.rss.collector.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.03.14
 */
public class CacheStub implements Cache {

    private final Map<Object, Object> objects;

    public CacheStub() {
        this.objects = new HashMap<>();
    }

    @Override
    public void put(final Object key, final Object object) {
        this.objects.put(key, object);
    }

    @Override
    public Object get(final Object key) {
        return this.objects.get(key);
    }

    @Override
    public boolean delete(final Object key) {
        return this.objects.remove(key) != null;
    }

    public void clear() {
        this.objects.clear();
    }

    public boolean isEmpty() {
        return this.objects.isEmpty();
    }

}
