package gmailgdogra.service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;

import static gmailgdogra.AppConstants.EXCEL_FILE_TYPE;

public class EmailService {

    private final String user = "gaurav.dogra.fullstack@gmail.com";
    private final String password = "8F6nrKV66H8Vn";


    public void sendmail(List<File> files) throws MessagingException {

        Properties properties = getProperties();
        Session session = getSession(properties);
        Message msg = getMessage(session);

        Multipart multipart = new MimeMultipart();
        msg.setContent(multipart);

        for (File file : files) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getName());
            messageBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
            messageBodyPart.setHeader("ContentType", EXCEL_FILE_TYPE);
            multipart.addBodyPart(messageBodyPart);
        }

        Transport.send(msg);
    }

    private Message getMessage(Session session) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(user));
        String to = "gaurav.dogra.fullstack@gmail.com";
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject("Error! reported from Excel Parsing app");
        return msg;
    }

    private Session getSession(Properties properties) {
        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

}
