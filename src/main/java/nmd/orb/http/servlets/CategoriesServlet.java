package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.categories.CategoriesServletDeleteRequestHandler;
import nmd.orb.http.servlets.categories.CategoriesServletGetRequestHandler;
import nmd.orb.http.servlets.categories.CategoriesServletPostRequestHandler;
import nmd.orb.http.servlets.categories.CategoriesServletPutRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServlet extends BaseServlet {

    public CategoriesServlet() {
        super();

        this.handlers.put(GET, new CategoriesServletGetRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper()));
        this.handlers.put(POST, new CategoriesServletPostRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper()));
        this.handlers.put(PUT, new CategoriesServletPutRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper()));
        this.handlers.put(DELETE, new CategoriesServletDeleteRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper()));
    }

}
