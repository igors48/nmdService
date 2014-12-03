package nmd.orb.services.importer;

import nmd.orb.reader.Category;

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
    private String categoryId;

    public CategoryImportContext(final String categoryName, final List<FeedImportContext> feedImportContexts, final CategoryImportTaskStatus status) {
        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(notNull(feedImportContexts));
        this.feedImportContexts = feedImportContexts;

        guard(notNull(status));
        this.status = status;

        this.categoryId = "";
    }

    public boolean canBeExecuted() {

        if (this.status.equals(CategoryImportTaskStatus.COMPLETED) || this.status.equals(CategoryImportTaskStatus.FAILED)) {
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

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(final String categoryId) {
        guard(Category.isValidCategoryId(categoryId));
        this.categoryId = categoryId;
    }

    public CategoryImportTaskStatus getStatus() {
        return this.status;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setStatus(final CategoryImportTaskStatus status) {
        guard(notNull(status));
        this.status = status;
    }

    //TODO tests
    public FeedImportContext findFirstExecutableTask(final FeedImportTaskStatus status) {
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
