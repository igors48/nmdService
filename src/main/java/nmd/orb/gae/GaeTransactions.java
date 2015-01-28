package nmd.orb.gae;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.repositories.Transactions;

/**
 * @author : igu
 */
public final class GaeTransactions implements Transactions {

    public static final GaeTransactions GAE_TRANSACTIONS = new GaeTransactions();

    @Override
    public Transaction beginOne() {
        return Datastore.INSTANCE.getDatastoreService().beginTransaction();
    }

    private GaeTransactions() {
        // empty
    }

}
