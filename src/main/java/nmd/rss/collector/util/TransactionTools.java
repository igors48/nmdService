package nmd.rss.collector.util;

import com.google.appengine.api.datastore.Transaction;

import javax.persistence.EntityTransaction;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public final class TransactionTools {

    //TODO remove
    public static void rollbackIfActive(final EntityTransaction transaction) {

        if (transaction != null) {

            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public static void rollbackIfActive(final Transaction transaction) {

        if (transaction != null) {

            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private TransactionTools() {
        // empty
    }
}
