package nmd.orb.services.importer;

import nmd.orb.error.ServiceException;

/**
 * Created by igor on 02.12.2014.
 */
public interface CategoriesServiceAdapter {

    String addCategory(String name) throws ServiceException;

}
