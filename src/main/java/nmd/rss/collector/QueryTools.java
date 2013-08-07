package nmd.rss.collector;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.06.13
 */
public final class QueryTools {

    public static String buildSelectAllQuery(final String entityName) {
        assertStringIsValid(entityName);

        return String.format("SELECT entity FROM %s entity", entityName);
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

    public static String buildDeleteAllQuery(final String entityName) {
        assertStringIsValid(entityName);

        return String.format("DELETE FROM %s entity", entityName);
    }

    private QueryTools() {
        // empty
    }

}
