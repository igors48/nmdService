package feed.repository;

import nmd.rss.collector.QueryTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.06.13
 */
public class QueryToolsTest {

    @Test
    public void buildSelectAllQuery() {
        assertEquals("SELECT entity FROM Table entity", QueryTools.buildSelectAllQuery("Table"));
    }

    @Test
    public void buildSelectWhereQuery() {
        assertEquals("SELECT entity FROM Table entity WHERE entity.field = :parameter", QueryTools.buildSelectWhereQuery("Table", "field", "parameter"));
    }

    @Test
    public void buildDeleteWhereQuery() {
        assertEquals("DELETE FROM Table entity WHERE entity.field = :parameter", QueryTools.buildDeleteWhereQuery("Table", "field", "parameter"));
    }

    @Test
    public void buildDeleteAllQuery() {
        assertEquals("DELETE FROM Table entity", QueryTools.buildDeleteAllQuery("Table"));
    }

}
