package nmd.orb.services;

import nmd.orb.feed.FeedHeader;
import nmd.orb.reader.Category;
import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.change.*;
import nmd.orb.services.export.Change;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeRegistrationService {

    private final ChangeRepository changeRepository;

    public ChangeRegistrationService(final ChangeRepository changeRepository) {
        guard(notNull(changeRepository));
        this.changeRepository = changeRepository;
    }

    public void registerChange() {
        final long timestamp = System.currentTimeMillis();
        final Change change = new Change(timestamp);

        this.changeRepository.store(change);
    }

    private void registerEvent(final Event event) {
        guard(notNull(event));
    }

    public void registerAddCategory(final String name) {
        guard(Category.isValidCategoryName(name));

        final AddCategoryEvent event = new AddCategoryEvent(name);

        registerEvent(event);
    }

    public void registerAssignFeedToCategory(final String feedTitle, final String categoryName)  {
        guard(FeedHeader.isValidFeedHeaderTitle(feedTitle));
        guard(Category.isValidCategoryName(categoryName));

        final AssignFeedToCategoryEvent event = new AssignFeedToCategoryEvent(feedTitle, categoryName);

        registerEvent(event);
    }

    public void registerDeleteCategory(final String name) {
        guard(Category.isValidCategoryName(name));

        final DeleteCategoryEvent event = new DeleteCategoryEvent(name);

        registerEvent(event);
    }

    public void registerRenameCategory(final String oldName, final String newName) {
        guard(Category.isValidCategoryName(oldName));
        guard(Category.isValidCategoryName(newName));

        final RenameCategoryEvent event = new RenameCategoryEvent(oldName, newName);

        registerEvent(event);
    }

    public void registerAddFeed(final String feedTitle, final String categoryName) {
        guard(FeedHeader.isValidFeedHeaderTitle(feedTitle));
        guard(Category.isValidCategoryName(categoryName));

        final AddFeedEvent event = new AddFeedEvent(feedTitle, categoryName);

        registerEvent(event);
    }

    public void registerRemoveFeed(final String feedTitle) {
        guard(FeedHeader.isValidFeedHeaderTitle(feedTitle));

        final RemoveFeedEvent event = new RemoveFeedEvent(feedTitle);

        registerEvent(event);
    }

    public void registerRenameFeed(final String oldName, final String newName) {

    }
}
