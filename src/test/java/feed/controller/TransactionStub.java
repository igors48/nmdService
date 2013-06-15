package feed.controller;

import javax.persistence.EntityTransaction;

import static feed.controller.TransactionState.*;

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
    public void rollback() {

        if (!isActive()) {
            throw new IllegalStateException();
        }

        this.state = ROLLED_BACK;
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
