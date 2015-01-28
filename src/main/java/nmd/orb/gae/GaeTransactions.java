package nmd.orb.gae;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.repositories.Transactions;

/**
 * @author : igu
 */
public enum GaeTransactions implements Transactions {

    INSTANCE;

    @Override
    public Transaction beginOne() {
        return Datastore.INSTANCE.getDatastoreService().beginTransaction();
    }

    private GaeTransactions() {
        // empty
    }

}
