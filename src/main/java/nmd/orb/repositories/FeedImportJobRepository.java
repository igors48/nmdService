package nmd.orb.repositories;

import nmd.orb.services.importer.FeedImportJob;

/**
 * @author : igu
 */
public interface FeedImportJobRepository {

    FeedImportJob load();

    void store(FeedImportJob job);

    void clear();

}
