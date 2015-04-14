package nmd.orb.services.content;

import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class SimpleBestNodeLocator implements BestNodeLocator {

    private static final int MAGIC_WORDS_IN_ATTRIBUTES_RATIO = 2;

    public TagNode findBest(final TagNode root) {
        guard(notNull(root));

        final ValuableContentNodesLocator valuableContentNodesLocator = new ValuableContentNodesLocator();

        root.traverse(valuableContentNodesLocator);

        final List<ValuableNode> valuableNodes = valuableContentNodesLocator.getValuableNodes();

        final Map<TagNode, List<ValuableNode>> groupedValuableNodes = groupByGrandparent(valuableNodes);

        return findBest(groupedValuableNodes);
    }

    private Map<TagNode, List<ValuableNode>> groupByGrandparent(final List<ValuableNode> valuableNodes) {
        final Map<TagNode, List<ValuableNode>> result = new HashMap<>();

        for (final ValuableNode node : valuableNodes) {
            final TagNode key = node.getGrandParent();

            if (result.get(key) == null) {
                result.put(key, new ArrayList<ValuableNode>());
            }

            final List<ValuableNode> entry = result.get(key);

            entry.add(node);
        }

        return result;
    }

    private TagNode findBest(final Map<TagNode, List<ValuableNode>> groups) {
        int maxRate = Integer.MIN_VALUE;
        TagNode result = null;

        for (final TagNode candidate : groups.keySet()) {

            if (notCommentNode(candidate)) {
                final int childrenRate = getChildrenRate(groups.get(candidate));
                final int finalRate = getNodeRate(candidate, childrenRate);

                if (finalRate > maxRate) {
                    maxRate = finalRate;
                    result = candidate;
                }
            }
        }

        return result;
    }

    private boolean notCommentNode(final TagNode candidate) {
        final Map<String, String> attributes = candidate.getAttributes();

        for (final String attribute : attributes.keySet()) {
            final String value = attributes.get(attribute).toLowerCase();

            if (value.contains("comment")) {
                return false;
            }
        }

        return true;
    }

    private int getChildrenRate(final List<ValuableNode> nodes) {
        int result = 0;

        for (final ValuableNode current : nodes) {
            result += current.getRate();
        }

        return result;
    }

    private int getNodeRate(final TagNode node, final int childrenRate) {
        final Map<String, String> attributes = node.getAttributes();

        for (final String attribute : attributes.keySet()) {
            final String value = attributes.get(attribute).toLowerCase();

            if (value.contains("news") || value.contains("entry") || value.contains("article")) {
                return childrenRate * MAGIC_WORDS_IN_ATTRIBUTES_RATIO;
            }
        }

        return childrenRate;
    }

}