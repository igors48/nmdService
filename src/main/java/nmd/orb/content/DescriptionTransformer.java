package nmd.orb.content;

import nmd.orb.content.element.Image;
import nmd.orb.content.element.PlainText;
import nmd.orb.util.UrlTools;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;

/**
 * Created by igor on 28.06.2015.
 */
public class DescriptionTransformer implements TagNodeVisitor {

    public final List<ContentElement> result;

    private final String base;

    public DescriptionTransformer(final String base) {
        guard(isValidUrl(this.base = base));

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

            if ("img".equalsIgnoreCase(tagName)) {
                final String src = tag.getAttributeByName("src");
                final boolean srcIsNotEmpty = src != null && !src.isEmpty();

                if (srcIsNotEmpty) {
                    final String normalizedSrc = UrlTools.normalize(this.base, src);
                    final Image image = new Image(normalizedSrc);

                    this.result.add(image);
                }
            }
        }

        return true;
    }

}
