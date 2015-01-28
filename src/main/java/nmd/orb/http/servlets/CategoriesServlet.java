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

    public static final CategoriesServletDeleteRequestHandler CATEGORIES_SERVLET_DELETE_REQUEST_HANDLER = new CategoriesServletDeleteRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper());
    public static final CategoriesServletGetRequestHandler CATEGORIES_SERVLET_GET_REQUEST_HANDLER = new CategoriesServletGetRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper());
    public static final CategoriesServletPostRequestHandler CATEGORIES_SERVLET_POST_REQUEST_HANDLER = new CategoriesServletPostRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper());
    public static final CategoriesServletPutRequestHandler CATEGORIES_SERVLET_PUT_REQUEST_HANDLER = new CategoriesServletPutRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper());

    public CategoriesServlet() {
        super();

        this.handlers.put(GET, CATEGORIES_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, CATEGORIES_SERVLET_POST_REQUEST_HANDLER);
        this.handlers.put(PUT, CATEGORIES_SERVLET_PUT_REQUEST_HANDLER);
        this.handlers.put(DELETE, CATEGORIES_SERVLET_DELETE_REQUEST_HANDLER);
    }

}
