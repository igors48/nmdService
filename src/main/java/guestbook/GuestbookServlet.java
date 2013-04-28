package guestbook;

import persistense.EMF;
import persistense.Record;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class GuestbookServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setContentType("text/plain");

        try {
            URL url = new URL("http://www.3dnews.ru/news/rss");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                resp.getWriter().println(line);
            }

            EntityManager entityManager = EMF.get().createEntityManager();

            TypedQuery<Record> query = entityManager.createQuery("select record from Record record", Record.class);
            List<Record> result = query.getResultList();

            for (Record record : result) {
                resp.getWriter().println(record.getValue());
            }

            reader.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}