package nmd.orb.content;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 28.06.2015.
 */
public class ContentTransformer {

    public static List<ContentElement> transform(final String content) {
        guard(notNull(content));

        final HtmlCleaner htmlCleaner = new HtmlCleaner();
        final TagNode rootNode = htmlCleaner.clean(content);

        final DescriptionTransformer visitor = new DescriptionTransformer();
        rootNode.traverse(visitor);

        return visitor.result;
    }

}
