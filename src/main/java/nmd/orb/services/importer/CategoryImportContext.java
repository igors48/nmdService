package nmd.orb.services.importer;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CategoryImportContext {

    private final String categoryName;
    private final List<FeedImportContext> feedImportContexts;

    private CategoryImportTaskStatus status;

    public CategoryImportContext(final String categoryName, final List<FeedImportContext> feedImportContexts, final CategoryImportTaskStatus status) {
        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(notNull(feedImportContexts));
        this.feedImportContexts = feedImportContexts;

        guard(notNull(status));
        this.status = status;
    }

    public boolean canBeExecuted() {

        if (this.status.equals(CategoryImportTaskStatus.COMPLETED)) {
            return false;
        }

        if (this.status.equals(CategoryImportTaskStatus.FEEDS_IMPORT)) {
            final FeedImportContext candidate = findFirstExecutableTask(FeedImportTaskStatus.WAITING);

            return candidate != null;
        }

        if (this.status.equals(CategoryImportTaskStatus.FEEDS_WITH_ERROR_IMPORT)) {
            final FeedImportContext candidate = findFirstExecutableTask(FeedImportTaskStatus.ERROR);

            return candidate != null;
        }

        return true;
    }

    private FeedImportContext findFirstExecutableTask(final FeedImportTaskStatus status) {
        final List<FeedImportContext> candidates = findTasks(status);

        for (final FeedImportContext candidate : candidates) {

            if (candidate.canBeExecuted()) {
                return candidate;
            }
        }

        return null;
    }

    private List<FeedImportContext> findTasks(final FeedImportTaskStatus status) {
        final List<FeedImportContext> result = new ArrayList<>();

        for (final FeedImportContext candidate : this.feedImportContexts) {

            if (candidate.getStatus().equals(status)) {
                result.add(candidate);
            }
        }

        return result;
    }

}
