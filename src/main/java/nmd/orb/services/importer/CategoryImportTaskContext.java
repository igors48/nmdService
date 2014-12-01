package nmd.orb.services.importer;

import java.util.List;

import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.services.importer.CategoryImportTaskStatus.CATEGORY_CREATE;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CategoryImportTaskContext {

    private final String categoryName;
    private final List<FeedImportTaskContext> feedImportTaskContexts;

    private CategoryImportTaskStatus status;
    private int current;

    public CategoryImportTaskContext(final String categoryName, final List<FeedImportTaskContext> feedImportTaskContexts) {
        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(notNull(feedImportTaskContexts));
        this.feedImportTaskContexts = feedImportTaskContexts;

        this.status = CATEGORY_CREATE;
        this.current = 0;
    }

}
