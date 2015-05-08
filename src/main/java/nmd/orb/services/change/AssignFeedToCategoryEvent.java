package nmd.orb.services.change;

import nmd.orb.feed.FeedHeader;
import nmd.orb.reader.Category;

import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class AssignFeedToCategoryEvent implements Event {

    public final String feedTitle;
    public final String categoryName;

    public AssignFeedToCategoryEvent(final String feedTitle, final String categoryName) {
        guard(FeedHeader.isValidFeedHeaderTitle(this.feedTitle = feedTitle));
        guard(Category.isValidCategoryName(this.categoryName = categoryName));
    }

}
