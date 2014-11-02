package nmd.orb.gae;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.repositories.Transactions;

import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.DATASTORE_SERVICE;

/**
 * @author : igu
 */
public final class GaeTransactions implements Transactions {

    public static final GaeTransactions GAE_TRANSACTIONS = new GaeTransactions();

    @Override
    public Transaction beginOne() {
        return DATASTORE_SERVICE.beginTransaction();
    }

    private GaeTransactions() {
        // empty
    }

}
