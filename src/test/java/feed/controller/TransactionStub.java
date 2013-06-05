package feed.controller;

import javax.persistence.EntityTransaction;

import static feed.controller.TransactionState.ACTIVE;
import static feed.controller.TransactionState.NOT_ACTIVE;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionStub implements EntityTransaction {

    private TransactionState state;

    public TransactionStub() {
        this.state = NOT_ACTIVE;
    }

    @Override
    public void begin() {

        if (isActive()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void commit() {

        if (!isActive()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void rollback() {

        if (!isActive()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void setRollbackOnly() {
        // empty
    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        return this.state == ACTIVE;
    }

    public TransactionState getState() {
        return this.state;
    }

}
