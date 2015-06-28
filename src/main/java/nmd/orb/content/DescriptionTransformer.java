package nmd.orb.content;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 28.06.2015.
 */
public class DescriptionTransformer implements TagNodeVisitor {

    public final List<ContentElement> result;

    public DescriptionTransformer() {
        this.result = new ArrayList<>();
    }

    @Override
    public boolean visit(final TagNode tagNode, final HtmlNode htmlNode) {

        if (htmlNode instanceof ContentNode) {
            final ContentNode contentNode = (ContentNode) htmlNode;
            final String content = contentNode.getContent();

            final PlainText plainText = new PlainText(content);
            this.result.add(plainText);
        }

        if (htmlNode instanceof TagNode) {
            final TagNode tag = (TagNode) htmlNode;
            final String tagName = tag.getName();

            if ("img".equals(tagName)) {
                final String src = tag.getAttributeByName("src");

                final Image image = new Image(src);
                this.result.add(image);
            }
        }

        return true;
    }

}
