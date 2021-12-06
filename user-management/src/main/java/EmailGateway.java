import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static util.Constants.NULL_PARAMETER;
import static util.Constants.SUCCESS;

public class EmailGateway {

    protected String to;
    protected String from;
    protected String password;

    public EmailGateway(String email) {
        to = email;
        getFromEmailPassword();
    }

    public void send() {
        Session session = Session.getInstance(getProps(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("poll-system: validate your email");
            message.setText("This is actual message");

            // Send message
            int returnCode = doSend(message);
            if (returnCode == NULL_PARAMETER)
                throw new NullPointerException("Null Parameter passed for to or from: " + to + from);
            else if (returnCode != SUCCESS) {
                throw new IllegalStateException("Unexpected error from emailing system #:" + returnCode);
            }

            System.out.println("Sent message successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private Properties getProps() {
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", "465");

        return properties;
    }

    public void getFromEmailPassword() {
        String filePath = "sender.json";
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream(filePath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, "UTF-8"));
            from = (String) jsonObject.get("email");
            password = (String) jsonObject.get("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int doSend(MimeMessage message) throws MessagingException {
        if (to == null || from == null) {
            return NULL_PARAMETER;
        }
        Transport.send(message);
        return SUCCESS;
    }
}
