package nmd.orb.services.importer;

import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.payload.CategoryPayload;
import nmd.orb.http.responses.payload.FeedHeaderPayload;
import nmd.orb.reader.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static nmd.orb.reader.Category.isValidCategoryName;
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
    private String categoryId;

    public CategoryImportContext(final String categoryName, final List<FeedImportContext> feedImportContexts, final CategoryImportTaskStatus status) {
        this(categoryName, feedImportContexts, status, "");
    }

    public CategoryImportContext(final String categoryName, final List<FeedImportContext> feedImportContexts, final CategoryImportTaskStatus status, final String categoryId) {
        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(notNull(feedImportContexts));
        this.feedImportContexts = feedImportContexts;

        guard(notNull(status));
        this.status = status;

        guard(notNull(categoryId));
        this.categoryId = "";
    }

    public boolean canBeExecuted() {

        return !(this.status.equals(CategoryImportTaskStatus.COMPLETED) || this.status.equals(CategoryImportTaskStatus.FAILED));

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

    public FeedImportContext findFirstExecutableTask(final FeedImportTaskStatus status) {
        final List<FeedImportContext> candidates = findTasks(status);

        for (final FeedImportContext candidate : candidates) {

            if (candidate.canBeExecuted()) {
                return candidate;
            }
        }

        return null;
    }

    public List<FeedImportContext> getFeedImportContexts() {
        return this.feedImportContexts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryImportContext context = (CategoryImportContext) o;

        if (!categoryId.equals(context.categoryId)) return false;
        if (!categoryName.equals(context.categoryName)) return false;
        if (!feedImportContexts.equals(context.feedImportContexts)) return false;
        if (status != context.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = categoryName.hashCode();
        result = 31 * result + feedImportContexts.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + categoryId.hashCode();
        return result;
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

    public static CategoryImportContext convert(final CategoryPayload categoryPayload, final Set<FeedHeaderPayload> feedHeaderPayloads, final int triesCount) throws ServiceException {
        guard(notNull(categoryPayload));
        guard(notNull(feedHeaderPayloads));
        guard(isPositive(triesCount));

        if (!Category.isValidCategoryName(categoryPayload.name)) {
            throw new ServiceException(ServiceError.invalidCategoryName(categoryPayload.name));
        }

        final List<FeedImportContext> contexts = new ArrayList<>();

        for (final FeedHeaderPayload feedHeaderPayload : feedHeaderPayloads) {
            contexts.add(FeedImportContext.convert(feedHeaderPayload, triesCount));
        }

        return new CategoryImportContext(categoryPayload.name, contexts, CategoryImportTaskStatus.CATEGORY_CREATE);
    }

}
