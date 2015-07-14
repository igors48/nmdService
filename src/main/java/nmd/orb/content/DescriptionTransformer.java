package nmd.orb.content;

import nmd.orb.content.element.Image;
import nmd.orb.content.element.PlainText;
import nmd.orb.content.element.PlainTextFromATag;
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
            visitContentNode(tagNode, (ContentNode) htmlNode);
        }

        if (htmlNode instanceof TagNode) {
            visitTagNode((TagNode) htmlNode);
        }

        return true;
    }

    private void visitTagNode(final TagNode tagNode) {
        final String tagName = tagNode.getName();

        if ("img".equalsIgnoreCase(tagName)) {
            handleImgTag(tagNode);
        }
    }

    private void visitContentNode(final TagNode tagNode, final ContentNode contentNode) {

        if (tagNode.getName().equalsIgnoreCase("a")) {
            handleATagContent(contentNode);
        } else {
            handleOtherTagsContent(contentNode);
        }
    }

    private void handleOtherTagsContent(final ContentNode contentNode) {
        final String content = contentNode.getContent();
        final ContentElement last = getLastElement();
        final boolean newPlainTextFromATagMustBeReplaced = last instanceof PlainTextFromATag;

        final PlainText plainText;

        if (newPlainTextFromATagMustBeReplaced) {
            this.result.remove(last);
            final PlainText oldPlainText = (PlainText) last;
            plainText = new PlainText(oldPlainText.text + content);
        } else {
            plainText = new PlainText(content);
        }

        this.result.add(plainText);
    }

    private void handleATagContent(final ContentNode contentNode) {
        final String content = contentNode.getContent();
        final ContentElement last = getLastElement();
        final boolean newPlainTextNeeded = !(last instanceof PlainText);

        final PlainText plainText;

        if (newPlainTextNeeded) {
            plainText = new PlainTextFromATag(content);
        } else {
            this.result.remove(last);
            final PlainText oldPlainText = (PlainText) last;
            plainText = new PlainTextFromATag(oldPlainText.text + content);
        }

        this.result.add(plainText);
    }

    private void handleImgTag(final TagNode tag) {
        final String src = tag.getAttributeByName("src");
        final boolean srcIsNotEmpty = src != null && !src.isEmpty();

        if (srcIsNotEmpty) {
            final String normalizedSrc = UrlTools.normalize(this.base, src);
            final Image image = new Image(normalizedSrc);

            this.result.add(image);
        }
    }

    private ContentElement getLastElement() {
        return this.result.isEmpty() ? null : this.result.get(this.result.size() - 1);
    }

}
