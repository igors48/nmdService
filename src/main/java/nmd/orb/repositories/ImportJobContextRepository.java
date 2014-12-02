package nmd.orb.repositories;

import nmd.orb.services.importer.ImportJobContext;

/**
 * @author : igu
 */
public interface ImportJobContextRepository {

    ImportJobContext load();

    void store(ImportJobContext job);

    void clear();

}
