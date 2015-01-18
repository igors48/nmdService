package nmd.orb.repositories;

import nmd.orb.services.export.Change;

/**
 * @author : igu
 */
public interface ChangeRepository {

    Change load();

    void store(Change context);

    void clear();

}
