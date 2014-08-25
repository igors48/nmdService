package unit.tool;

import org.junit.Test;

import static nmd.orb.util.StringTools.cutTo;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.03.2014
 */
public class StringToolsTest {

    @Test
    public void smoke() {
        assertEquals("abc", cutTo("abc", 5));
        assertEquals("abc", cutTo("abc", 3));
        assertEquals("ab", cutTo("abc", 2));
        assertEquals("", cutTo("abc", 0));
    }

}
