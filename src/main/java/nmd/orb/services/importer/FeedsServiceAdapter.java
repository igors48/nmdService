package nmd.orb.services.importer;

import nmd.orb.error.ServiceException;

import java.util.UUID;

/**
 * Created by igor on 02.12.2014.
 */
public interface FeedsServiceAdapter {

    UUID addFeed(String feedLink, String feedTitle, String categoryId) throws ServiceException;

}
