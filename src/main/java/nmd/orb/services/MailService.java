package nmd.orb.services;

import com.google.gson.Gson;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.ExportReportResponse;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
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
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);

            final String exportEmail = System.getProperty("export.email");

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(exportEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(exportEmail));
            message.setSubject("Exported feeds and categories");

            final Multipart multipart = new MimeMultipart();

            final MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent("Please find exported feeds and categories in the attachment", "text/html");
            multipart.addBodyPart(htmlPart);

            final MimeBodyPart attachment = new MimeBodyPart();

            attachment.setFileName("export.json");
            final String reportAsJson = GSON.toJson(report);
            DataSource dataSource = new ByteArrayDataSource(reportAsJson.getBytes("UTF-8"), "application/octet-stream");
            attachment.setDataHandler(new DataHandler(dataSource));
            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception exception) {
            throw new ServiceException(mailServiceError(), exception);
        }
    }

}
