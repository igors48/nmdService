package nmd.orb.gae.repositories.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * @author : igu
 */
public enum Datastore {

    INSTANCE;

    private final DatastoreService datastoreService;

    private Datastore() {
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public DatastoreService getDatastoreService() {
        return datastoreService;
    }

}
