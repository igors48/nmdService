package nmd.rss.collector;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.06.13
 */
public class EntityManagerTransactions implements Transactions {

    private final EntityManager entityManager;

    public EntityManagerTransactions(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public EntityTransaction getOne() {
        return this.entityManager.getTransaction();
    }

}
