package unit.tool;

import org.junit.Test;

import java.util.ArrayList;

import static nmd.rss.collector.rest.tools.ServletTools.parse;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.01.14
 */
public class ServletToolsParsePathInfoTest {

    @Test
    public void extractElementsPart() {
        assertEquals(new ArrayList<String>(), parse(null));
        assertEquals(new ArrayList<String>(), parse(""));
        assertEquals(new ArrayList<String>(), parse("/"));
        assertEquals(new ArrayList<String>(), parse("///"));
        assertEquals(new ArrayList<String>() {{
            add("a");
        }}, parse("/a"));
        assertEquals(new ArrayList<String>() {{
            add("a");
        }}, parse("a"));
        assertEquals(new ArrayList<String>() {{
            add("a");
            add("b");
        }}, parse("a/b"));
        assertEquals(new ArrayList<String>() {{
            add("a");
            add("b");
        }}, parse("/a/b"));
        assertEquals(new ArrayList<String>() {{
            add("a");
            add("b");
        }}, parse("/a/b/"));
        assertEquals(new ArrayList<String>() {{
            add("a");
            add("b");
        }}, parse("/a//b/"));
    }

}
