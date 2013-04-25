package guestbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.*;

public class GuestbookServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        try {
            URL url = new URL("http://www.3dnews.ru/news/rss");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                resp.getWriter().println(line);
            }
            reader.close();

        } catch (IOException e) {
            // ...
        }
    }
    
}