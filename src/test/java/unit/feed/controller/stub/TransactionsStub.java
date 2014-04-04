package unit.feed.controller.stub;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionsStub implements Transactions {

    private Transaction previous;

    @Override
    public Transaction beginOne() {

        assertOpenedTransactionNotActive();

        return this.previous = new TransactionStub();
    }

    public void assertOpenedTransactionNotActive() {

        if (this.previous != null) {

            if (this.previous.isActive()) {
                throw new IllegalStateException("Not closed transaction");
            }
        }
    }

}
