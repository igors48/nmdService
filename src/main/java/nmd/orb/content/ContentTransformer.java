package nmd.orb.content;

import org.htmlcleaner.*;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 28.06.2015.
 */
public class ContentTransformer {

    public static List<ContentElement> transform(final String content) {
        guard(notNull(content));

        final List<ContentElement> result = new ArrayList<>();

        final HtmlCleaner htmlCleaner = new HtmlCleaner();
        final TagNode rootNode = htmlCleaner.clean(content);

        rootNode.traverse(new TagNodeVisitor() {
            public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
                if (htmlNode instanceof TagNode) {
                    TagNode tag = (TagNode) htmlNode;
                    String tagName = tag.getName();
                    if ("img".equals(tagName)) {
                        String src = tag.getAttributeByName("src");
                        if (src != null) {
                            //tag.setAttribute("src", Utils.fullUrl(siteUrl, src));
                        }
                    }
                } else if (htmlNode instanceof CommentNode) {
                    CommentNode comment = ((CommentNode) htmlNode);
                    //comment.getContent().append(" -- By HtmlCleaner");
                }
                // tells visitor to continue traversing the DOM tree
                return true;
            }
        });

        return result;
    }

}
