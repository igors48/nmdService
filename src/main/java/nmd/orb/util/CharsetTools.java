package nmd.orb.util;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 14.05.13
 */
public final class CharsetTools {

    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(.*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern ENCODING_PATTERN = Pattern.compile("encoding=\"(.+?)\"", Pattern.CASE_INSENSITIVE);

    private static final String UTF_8 = "UTF-8";
    private static final String WINDOWS_1251 = "windows-1251";
    private static final String ISO_8859_1 = "ISO-8859-1";

    private static final Set<String> CHARSETS = new HashSet<String>() {{
        add(UTF_8);
        add(WINDOWS_1251);
        add(ISO_8859_1);
    }};

    public static String detectCharset(final byte[] bytes) {

        for (final String charset : CHARSETS) {
            String foundCharset = tryCharset(bytes, charset);

            if (foundCharset != null) {
                return foundCharset;
            }
        }

        return UTF_8;
    }

    public static String tryCharset(final byte[] bytes, String charsetName) {

        try {
            String string = new String(bytes, charsetName);
            String foundCharsetName = CharsetTools.findCharSet(string);
            Charset.forName(foundCharsetName);

            return foundCharsetName;
        } catch (Exception exception) {
            return null;
        }
    }

    public static String findCharSet(final String data) {
        assertNotNull(data);

        String result = null;

        final Matcher charsetPatternMatcher = CHARSET_PATTERN.matcher(data);

        if (charsetPatternMatcher.find()) {
            result = charsetPatternMatcher.group(1);
        } else {
            final Matcher encodingPatternMatcher = ENCODING_PATTERN.matcher(data);

            if (encodingPatternMatcher.find()) {
                result = encodingPatternMatcher.group(1);
            }
        }

        return result;
    }

    private CharsetTools() {
        // empty
    }

}
