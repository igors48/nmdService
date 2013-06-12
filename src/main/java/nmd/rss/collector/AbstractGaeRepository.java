package nmd.rss.collector;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.06.13
 */
public abstract class AbstractGaeRepository implements Repository {

    private final EntityManager entityManager;
    private final String entityName;

    protected AbstractGaeRepository(final EntityManager entityManager, final String entityName) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;

        assertStringIsValid(entityName);
        this.entityName = entityName;
    }

    @Override
    public List loadAllEntities() {
        return buildSelectAllQuery(Object.class).getResultList();
    }

    @Override
    public void removeEntity(final Object victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
    }

    protected <T> TypedQuery<T> buildSelectWhereQuery(final String field, final String parameter, final Class<T> clazz) {
        final String query = QueryTools.buildSelectWhereQuery(this.entityName, field, parameter);

        return this.entityManager.createQuery(query, clazz);
    }

    protected <T> TypedQuery<T> buildDeleteWhereQuery(final String field, final String parameter, final Class<T> clazz) {
        final String query = QueryTools.buildDeleteWhereQuery(this.entityName, field, parameter);

        return this.entityManager.createQuery(query, clazz);
    }

    protected <T> TypedQuery<T> buildSelectAllQuery(final Class<T> clazz) {
        final String query = QueryTools.buildSelectAllQuery(this.entityName);

        return this.entityManager.createQuery(query, clazz);
    }

}
