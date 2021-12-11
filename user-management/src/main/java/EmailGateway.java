import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

import static util.Constants.NULL_PARAMETER;
import static util.Constants.SUCCESS;

public class EmailGateway {

    public static final EmailGateway INSTANCE = (EmailGateway) PluginFactory.getPlugin(EmailGateway.class);

    protected String to;
    protected String from;
    protected String password;

    public EmailGateway() {
        getSenderEmailPassword();
    }

    public void send(String email, String name, String token, String type) {
        to = email;
        Session session = Session.getInstance(getProps(), getPasswordAuthentication());

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("poll-system: validate your email");
            message.setContent(process(email, name, token, type), "text/html");

            // Send message
            int returnCode = doSend(message);
            if (returnCode == NULL_PARAMETER)
                throw new NullPointerException("Null Parameter passed for to or from: " + to + from);
            else if (returnCode != SUCCESS) {
                throw new IllegalStateException("Unexpected error from emailing system #:" + returnCode);
            }

            System.out.println("Sent message successfully.");
        } catch (MessagingException | ParserConfigurationException mex) {
            mex.printStackTrace();
        }
    }

    // creating an Authenticator with the email and password from config
    protected Authenticator getPasswordAuthentication() {
        return new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
    }

    // setting the mail server props
    protected Properties getProps() {
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

    // reading the sender email and pass from the config file in resources/sender.json
    protected void getSenderEmailPassword() {
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

    // separating the actual sending to another method for ease of testing
    int doSend(MimeMessage message) throws MessagingException {
        if (to == null || from == null) {
            return NULL_PARAMETER;
        }
        Transport.send(message);
        return SUCCESS;
    }

    //Transform view data
    public String process(String email, String name, String token, String type) throws ParserConfigurationException {
        String XSLFILE = "template.xsl";
        InputStream xslresource = getClass().getClassLoader().getResourceAsStream(XSLFILE);
        StreamSource xslcode = new StreamSource(xslresource);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer trans = tf.newTransformer(xslcode);
            trans.transform(new DOMSource(createXML(email, name, token, type)), result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private Document createXML(String email, String name, String token, String type) throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        //Starts the root element.
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("class");
        doc.appendChild(root);

        Element username = doc.createElement("name");
        username.appendChild(doc.createTextNode(name));
        root.appendChild(username);

        Element usertoken = doc.createElement("href");
        usertoken.appendChild(doc.createTextNode(constructLink(email, token, type)));
        root.appendChild(usertoken);
        return doc;
    }

    private String constructLink(String email, String token, String type) {
        return String.format("http://localhost:3000/login?email=%s&token=%s&type=%s", email, token, type);
    }
}
