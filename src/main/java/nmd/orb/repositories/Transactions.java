package nmd.orb.repositories;

import com.google.appengine.api.datastore.Transaction;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.06.13
 */
public interface Transactions {

    Transaction beginOne();

}
