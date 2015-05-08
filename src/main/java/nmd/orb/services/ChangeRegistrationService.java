package nmd.orb.services;

import nmd.orb.reader.Category;
import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.change.AddCategoryEvent;
import nmd.orb.services.change.Event;
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
    /*
    public void registerAssignFeedToCategory(final String feedTitle, final String categoryName)
    public void registerDeleteCategory(final String name)
    public void registerRenameCategory(final String oldName, final String newName)
    public void registerAddFeed(final String feedTitle, final String categoryName)
    public void registerRemoveFeed(final String feedTitle)
    public void registerRenameFeed(final String oldName, final String newName)
    */
}
