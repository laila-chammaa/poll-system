import javax.mail.internet.MimeMessage;

import static util.Constants.*;

public class EmailGatewayStub extends EmailGateway {
    // used in testing
    private boolean shouldFailAllMessages = false;

    protected int doSend(MimeMessage message) {
        if (shouldFailAllMessages) return -999;
        if (to == null || from == null) {
            return NULL_PARAMETER;
        }
        return SUCCESS;
    }

    public void failAllMessages() {
        shouldFailAllMessages = true;
    }
}
