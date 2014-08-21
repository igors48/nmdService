package nmd.rss.collector.util;

import org.htmlcleaner.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * @author : igu
 */
public final class HtmlTools {

    private static final Logger LOGGER = Logger.getLogger(HtmlTools.class.getName());

    private static final HtmlCleaner CLEANER;
    private static final Serializer SERIALIZER;

    static {
        CLEANER = new HtmlCleaner();

        final CleanerProperties PROPERTIES = CLEANER.getProperties();
        PROPERTIES.setOmitXmlDeclaration(true);

        SERIALIZER = new CompactHtmlSerializer(PROPERTIES);
    }

    public static String cleanupDescription(final String html) {
        guard(notNull(html));

        try {
            final TagNode node = CLEANER.clean(html);
            Object[] nodes = node.evaluateXPath("/body");

            if (nodes.length > 0) {
                final TagNode firstNode = (TagNode) nodes[0];

                return SERIALIZER.getAsString(firstNode, "UTF-8", true);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error cleaning description html", exception);
        }

        return html;
    }

    private HtmlTools() {

    }
}
