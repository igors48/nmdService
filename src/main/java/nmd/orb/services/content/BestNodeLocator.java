package nmd.orb.services.content;

import org.htmlcleaner.TagNode;

/**
 * @author : igu
 */
public interface BestNodeLocator {

    TagNode findBest(TagNode root);

}