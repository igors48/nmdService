package nmd.rss.collector.gae;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;

import static nmd.rss.collector.gae.persistence.GaeDatastoreTools.DATASTORE_SERVICE;

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

    }

}
