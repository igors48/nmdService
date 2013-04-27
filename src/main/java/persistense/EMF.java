package persistense;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.04.13
 */
public class EMF {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("transactions-optional");

    private EMF() {
    }

    public static EntityManagerFactory get() {
        return ENTITY_MANAGER_FACTORY;
    }

}
