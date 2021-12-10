import javax.mail.internet.MimeMessage;

import static util.Constants.*;

public class EmailGatewayStub extends EmailGateway {
    // used in testing
    private boolean shouldFailAllMessages = false;
    private int sentEmails = 0;

    EmailGatewayStub() {
        super();
    }

    protected int doSend(MimeMessage message) {
        if (shouldFailAllMessages) return -999;
        if (to == null || from == null) {
            return NULL_PARAMETER;
        }
        sentEmails++;
        return SUCCESS;
    }

    public void failAllMessages() {
        shouldFailAllMessages = true;
    }

    public int getNumberOfEmailsSent() {
        return sentEmails;
    }

    public void reset() {
        shouldFailAllMessages = false;
        sentEmails = 0;
    }
}
