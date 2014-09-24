package nmd.orb.util;

import org.htmlcleaner.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public final class CleanupTools {

    private static final Logger LOGGER = Logger.getLogger(CleanupTools.class.getName());

    private static final HtmlCleaner CLEANER;

    private static final Serializer HTML_SERIALIZER;
    private static final Serializer XML_SERIALIZER;

    static {
        CLEANER = new HtmlCleaner();

        final CleanerProperties PROPERTIES = CLEANER.getProperties();
        PROPERTIES.setOmitXmlDeclaration(true);

        HTML_SERIALIZER = new CompactHtmlSerializer(PROPERTIES);
        XML_SERIALIZER = new SimpleHtmlSerializer(PROPERTIES);
    }

    public static String cleanupDescription(final String html) {
        guard(notNull(html));

        try {
            final TagNode node = CLEANER.clean(html);
            Object[] nodes = node.evaluateXPath("/body");

            if (nodes.length > 0) {
                final TagNode firstNode = (TagNode) nodes[0];

                return HTML_SERIALIZER.getAsString(firstNode, "UTF-8", true);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error cleaning description html", exception);
        }

        return html;
    }

    public static String cleanupXml(final byte[] data) {
        guard(notNull(data));

        try {
            final InputStream inputStream = new ByteArrayInputStream(data);
            final TagNode tagNode = CLEANER.clean(inputStream);

            Object[] nodes = tagNode.evaluateXPath("/body");

            if (nodes.length > 0) {
                final TagNode firstNode = (TagNode) nodes[0];

                return XML_SERIALIZER.getAsString(firstNode);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error cleaning description xml", exception);
        }

        return "";
    }

    private CleanupTools() {

    }
}
