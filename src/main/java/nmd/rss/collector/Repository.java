package nmd.rss.collector;

import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.06.13
 */
public interface Repository {

    List loadAllEntities();

    void removeEntity(Object victim);

}
