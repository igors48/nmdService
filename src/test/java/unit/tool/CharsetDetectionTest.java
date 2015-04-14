package unit.tool;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static nmd.orb.util.ConnectionTools.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 21.02.2015.
 */
public class CharsetDetectionTest {

    @Test
    public void whenNoHeadersThenUtf8IsReturned() throws Exception {
        final Map<String, List<String>> headers = new HashMap<>();

        assertEquals(UTF_8, getCharset(headers));
    }

    @Test
    public void whenNoContentEncodingHeaderThenUtf8IsReturned() throws Exception {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_ENCODING, asList("value"));

        assertEquals(UTF_8, getCharset(headers));
    }

    @Test
    public void whenContentEncodingHeaderExistsThenCharsetIsReturned() throws Exception {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_TYPE, asList("charset=windows-1251"));

        assertEquals("windows-1251", getCharset(headers));
    }

    @Test
    public void lastSemicolonIsIgnored() throws Exception {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_TYPE, asList("charset=windows-1251;"));

        assertEquals("windows-1251", getCharset(headers));
    }

    @Test
    public void whenThereAreSeveralHeaderThenFirstAppropriateIsUsed() throws Exception {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_TYPE, asList("something", "charset=windows-1251;", "charset=utf-8"));

        assertEquals("windows-1251", getCharset(headers));
    }

}
