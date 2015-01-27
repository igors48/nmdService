package nmd.orb.repositories;

import nmd.orb.services.export.Change;

/**
 * @author : igu
 */
public interface ChangeRepository {

    Change load();

    void store(Change change);

    void clear();

}
