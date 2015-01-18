package nmd.orb.services;

import com.google.gson.Gson;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.http.tools.ContentType;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

import static nmd.orb.error.ServiceError.mailServiceError;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class MailService {

    private static final Gson GSON = new Gson();

    public void sendChangeNotification(final ExportReportResponse report) throws ServiceException {
        guard(notNull(report));

        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("igors48@gmail.com", "Mr. User"));
            message.setSubject("subject");

            final String reportAsJson = GSON.toJson(report);
            final byte[] reportAsBytes = reportAsJson.getBytes("UTF-8");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("export", "text/html");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName("export.json");
            attachment.setContent(reportAsBytes, ContentType.JSON.mime);
            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception exception) {
            throw new ServiceException(mailServiceError(), exception);
        }
    }

}
