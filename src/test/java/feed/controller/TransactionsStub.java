package feed.controller;

import nmd.rss.collector.Transactions;

import javax.persistence.EntityTransaction;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.06.13
 */
public class TransactionsStub implements Transactions {

    @Override
    public EntityTransaction getOne() {
        return new TransactionStub();
    }

}
