package unit.feed.controller.stub;

import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.export.Change;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeRepositoryStub implements ChangeRepository {

    private Change change;

    @Override
    public Change load() {
        return this.change;
    }

    @Override
    public void store(final Change change) {
        this.change = change;
    }

    @Override
    public void clear() {
        this.change = null;
    }

    public boolean isEmpty() {
        return this.change == null;
    }

}
