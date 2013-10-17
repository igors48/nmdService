package nmd.rss.collector.util;

import com.google.appengine.api.datastore.Transaction;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public final class TransactionTools {

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
