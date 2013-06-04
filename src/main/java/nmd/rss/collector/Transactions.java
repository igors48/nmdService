package nmd.rss.collector;

import javax.persistence.EntityTransaction;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.06.13
 */
public interface Transactions {

    EntityTransaction getOne();

}
