package feed.controller;

import com.google.appengine.api.datastore.Transaction;

import java.util.concurrent.Future;

import static feed.controller.TransactionState.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionStub implements Transaction {

    private TransactionState state;

    public TransactionStub() {
        this.state = ACTIVE;
    }

    @Override
    public void commit() {

        if (!isActive()) {
            throw new IllegalStateException();
        }

        this.state = COMMITTED;
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

        this.state = ROLLED_BACK;
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
        return this.state == ACTIVE;
    }

}
