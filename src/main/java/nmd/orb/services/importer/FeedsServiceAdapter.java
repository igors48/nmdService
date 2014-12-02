package nmd.orb.services.importer;

import nmd.orb.error.ServiceException;

/**
 * Created by igor on 02.12.2014.
 */
public interface FeedsServiceAdapter {

    void addFeed(String feedLink, String feedTitle, String categoryId) throws ServiceException;

}
