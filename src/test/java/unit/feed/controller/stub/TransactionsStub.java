package unit.feed.controller.stub;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionsStub implements Transactions {

    @Override
    public Transaction beginOne() {
        return new TransactionStub();
    }

}
