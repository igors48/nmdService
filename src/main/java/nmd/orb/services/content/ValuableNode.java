package nmd.orb.services.content;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ValuableNode {

    private final ContentNode contentNode;
    private final TagNode parent;
    private final TagNode grandParent;
    private final int rate;

    public ValuableNode(final ContentNode contentNode, final TagNode parent, final TagNode grandParent, final int rate) {
        guard(notNull(contentNode));
        this.contentNode = contentNode;

        guard(notNull(parent));
        this.parent = parent;

        guard(notNull(grandParent));
        this.grandParent = grandParent;

        guard(isPositive(rate));
        this.rate = rate;
    }

    public ContentNode getContentNode() {
        return contentNode;
    }

    public TagNode getParent() {
        return parent;
    }

    public TagNode getGrandParent() {
        return grandParent;
    }

    public int getRate() {
        return rate;
    }

}