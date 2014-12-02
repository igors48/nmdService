package nmd.orb.services.importer;

import java.util.List;

import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.services.importer.CategoryImportTaskStatus.CATEGORY_CREATE;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CategoryImportContext {

    private final String categoryName;
    private final List<FeedImportContext> feedImportContexts;

    private CategoryImportTaskStatus status;
    private int current;

    public CategoryImportContext(final String categoryName, final List<FeedImportContext> feedImportContexts, final int current, final CategoryImportTaskStatus status) {
        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(notNull(feedImportContexts));
        this.feedImportContexts = feedImportContexts;

        guard(isPositive(current));
        this.current = 0;

        guard(notNull(status));
        this.status = CATEGORY_CREATE;
    }

    public boolean canBeExecuted() {
        return false;
    }

}
