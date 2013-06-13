package nmd.rss.collector;

import static nmd.rss.collector.util.Assert.assertPositive;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.06.13
 */
public final class QueryTools {

    public static String buildSelectAllQuery(final String entityName, final int limit) {
        assertStringIsValid(entityName);
        assertPositive(limit);

        String base = String.format("SELECT entity FROM %s entity", entityName);

        if (limit > 0) {
            base += String.format(" LIMIT %d", limit);
        }

        return base;
    }

    public static String buildSelectWhereQuery(final String entityName, final String field, final String parameter) {
        assertStringIsValid(entityName);
        assertStringIsValid(field);
        assertStringIsValid(parameter);

        return String.format("SELECT entity FROM %s entity WHERE entity.%s = :%s", entityName, field, parameter);
    }

    public static String buildDeleteWhereQuery(final String entityName, final String field, final String parameter) {
        assertStringIsValid(entityName);
        assertStringIsValid(field);
        assertStringIsValid(parameter);

        return String.format("DELETE FROM %s entity WHERE entity.%s = :%s", entityName, field, parameter);
    }

    private QueryTools() {
        // empty
    }

}
