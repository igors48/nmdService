package unit.tool;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nmd.orb.util.ConnectionTools.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 17.02.2015.
 */
public class GZipDetectionTest {

    @Test
    public void whenNoHeadersThenNotGZipped() {
        final Map<String, List<String>> headers = new HashMap<>();

        assertFalse(ifGZipped(headers));
    }

    @Test
    public void whenNoContentEncodingHeaderThenNotGZipped() {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put("asd", Arrays.asList("value"));

        assertFalse(ifGZipped(headers));
    }

    @Test
    public void whenContentEncodingHeaderDoesNotContainGZipThenNotGZipped() {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_ENCODING, Arrays.asList("value", "another"));

        assertFalse(ifGZipped(headers));
    }

    @Test
    public void whenContentEncodingHeaderContainGZipThenGZipped() {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_ENCODING, Arrays.asList(GZIP, "another"));

        assertTrue(ifGZipped(headers));
    }

    @Test
    public void whenContentEncodingHeaderContainGZipIgnoreCasedThenGZipped() {
        final Map<String, List<String>> headers = new HashMap<>();

        headers.put(CONTENT_ENCODING.toLowerCase(), Arrays.asList(GZIP.toLowerCase(), "another"));

        assertTrue(ifGZipped(headers));
    }

}
