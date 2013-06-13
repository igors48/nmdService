package nmd.rss.collector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public void remove(final Object victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
    }

    protected void persist(final Object object) {
        this.entityManager.persist(object);
    }

    protected <T> TypedQuery<T> buildSelectWhereQuery(final String field, final Object value, final Class<T> clazz) {
        final String template = QueryTools.buildSelectWhereQuery(this.entityName, field, field);
        final TypedQuery<T> query = this.entityManager.createQuery(template, clazz);
        query.setParameter(field, value);

        return query;
    }

    protected Query buildDeleteWhereQuery(final String field, final Object value) {
        final String template = QueryTools.buildDeleteWhereQuery(this.entityName, field, field);
        final Query query = this.entityManager.createQuery(template);
        query.setParameter(field, value);

        return query;
    }

    protected <T> TypedQuery<T> buildSelectAllQuery(final Class<T> clazz) {
        return buildSelectAllQuery(clazz, 0);
    }

    protected <T> TypedQuery<T> buildSelectAllQuery(final Class<T> clazz, int limit) {
        final String query = QueryTools.buildSelectAllQuery(this.entityName, limit);

        return this.entityManager.createQuery(query, clazz);
    }

}
