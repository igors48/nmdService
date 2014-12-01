package nmd.orb.repositories;

import nmd.orb.services.importer.ImportJob;

/**
 * @author : igu
 */
public interface FeedImportJobRepository {

    ImportJob load();

    void store(ImportJob job);

    void clear();

}
