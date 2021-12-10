import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.*;

public class EmailGatewayTest {
    private EmailGatewayStub newGate() {
        return (EmailGatewayStub) EmailGateway.INSTANCE;
    }
    private String token = "fake";
    private String name = "fake";

    @Test
    public void testSendNullArg() {
        EmailGatewayStub stub = (EmailGatewayStub) EmailGateway.INSTANCE;
        try {
            stub.send(null, name, token, "signup");
            Assert.fail("Didn’t detect null argument");
        } catch (NullPointerException expected) {
        }
        assertEquals(0, stub.getNumberOfEmailsSent());
    }

    @Test
    public void testSendCorrectArg() {
        EmailGatewayStub stub = (EmailGatewayStub) EmailGateway.INSTANCE;
        stub.send("layla@email.com", name, token, "signup");
        assertEquals(1, stub.getNumberOfEmailsSent());
    }

    @Test
    public void testGatewayCrash() {
        EmailGatewayStub stub = (EmailGatewayStub) EmailGateway.INSTANCE;
        stub.failAllMessages();
        try {
            stub.send("layla@email.com", name, token, "signup");
            Assert.fail("Didn’t detect anything wrong");
        } catch (IllegalStateException expected) {
        }
        assertEquals(0, stub.getNumberOfEmailsSent());
    }
}
