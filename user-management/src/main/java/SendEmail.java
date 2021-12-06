import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    private String to;
    private String from;

    public static void main(String [] args) {
        String host = "localhost";
        // Get system properties
        Properties properties = System.getProperties();
        String to = "laylachammaa@gmail.com";
        String from = "laylachammaa@gmail.com";
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("poll-system: validate your email");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            doSend(message);
            System.out.println("Sent message successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    static void doSend(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }
}
