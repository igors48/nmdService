package unit.feed.controller.stub;

import com.google.appengine.api.datastore.Transaction;

import java.util.concurrent.Future;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionStub implements Transaction {

    private TransactionState state;

    public TransactionStub() {
        this.state = TransactionState.ACTIVE;
    }

    @Override
    public void commit() {

        if (!isActive()) {
            throw new IllegalStateException();
        }

        this.state = TransactionState.COMMITTED;
    }

    @Override
    public Future<Void> commitAsync() {
        return null;
    }

    @Override
    public void rollback() {

        if (!isActive()) {
            throw new IllegalStateException();
        }

        this.state = TransactionState.ROLLED_BACK;
    }

    @Override
    public Future<Void> rollbackAsync() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getApp() {
        return null;
    }

    @Override
    public boolean isActive() {
        return this.state == TransactionState.ACTIVE;
    }

}
