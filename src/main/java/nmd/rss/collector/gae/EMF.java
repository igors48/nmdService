package nmd.rss.collector.gae;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.04.13
 */
public final class EMF {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("transactional");

    private EMF() {
    }

    public static EntityManagerFactory get() {
        return ENTITY_MANAGER_FACTORY;
    }

}
