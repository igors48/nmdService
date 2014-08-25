package nmd.orb.collector;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.03.14
 */
public interface Cache {

    void put(Object key, Object object);

    Object get(Object key);

    boolean delete(Object key);

}
