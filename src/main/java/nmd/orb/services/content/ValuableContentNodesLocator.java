package nmd.orb.services.content;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : igu
 */
public class ValuableContentNodesLocator implements TagNodeVisitor {

    private static final int MIN_RATE = 20;

    private final List<ValuableNode> valuableNodes;

    public ValuableContentNodesLocator() {
        this.valuableNodes = new ArrayList<>();
    }

    public boolean visit(final TagNode parent, final HtmlNode candidate) {

        if (candidateNode(parent)) {

            if (candidate instanceof ContentNode) {
                final ContentNode contentNode = (ContentNode) candidate;

                final int rate = getRate(contentNode);

                if (rate > MIN_RATE) {
                    valuableNodes.add(new ValuableNode(contentNode, parent, parent.getParent(), rate));
                }
            }
        }

        return true;
    }

    public List<ValuableNode> getValuableNodes() {
        return valuableNodes;
    }

    private boolean candidateNode(final TagNode node) {
        return node != null && (node.getName().equalsIgnoreCase("p") || node.getName().equalsIgnoreCase("div"));
    }

    private int getRate(final ContentNode node) {
        final String cleaned = node.getContent().replaceAll("\\s", "");

        return cleaned.length();
    }
}