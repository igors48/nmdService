package cron;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.04.13
 */
public class CronServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CronServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(String.format("doGet [ %d ]", System.currentTimeMillis()));


        super.doGet(request, response);
    }

}
