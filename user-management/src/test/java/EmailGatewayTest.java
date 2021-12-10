import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.*;

public class EmailGatewayTest {
    private EmailGatewayStub gate() {
        return (EmailGatewayStub) EmailGateway.INSTANCE;
    }
    private String token = "fake";
    private String name = "fake";

    @Test
    public void testSendNullArg() {
        try {
            gate().send(null, name, token);
            Assert.fail("Didn’t detect null argument");
        } catch (NullPointerException expected) {
        }
        assertEquals(0, gate().getNumberOfEmailsSent());
    }

    @Test
    public void testSendCorrectArg() {
        gate().send("layla@email.com", name, token);
        assertEquals(1, gate().getNumberOfEmailsSent());
    }

    @Test
    public void testGatewayCrash() {
        gate().failAllMessages();
        try {
            gate().send("layla@email.com", name, token);
            Assert.fail("Didn’t detect anything wrong");
        } catch (IllegalStateException expected) {
        }
        assertEquals(0, gate().getNumberOfEmailsSent());
    }
}
