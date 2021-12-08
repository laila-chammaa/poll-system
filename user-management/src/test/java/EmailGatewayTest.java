import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.*;

public class EmailGatewayTest {
    private EmailGatewayStub gate() {
        return (EmailGatewayStub) EmailGateway.INSTANCE;
    }

    @Test
    public void testSendNullArg() {
        try {
            gate().send(null);
            Assert.fail("Didn’t detect null argument");
        } catch (NullPointerException expected) {
        }
        assertEquals(0, gate().getNumberOfEmailsSent());
    }

    @Test
    public void testSendCorrectArg() {
        gate().send("layla@email.com");
        assertEquals(1, gate().getNumberOfEmailsSent());
    }

    @Test
    public void testGatewayCrash() {
        gate().failAllMessages();
        try {
            gate().send("layla@email.com");
            Assert.fail("Didn’t detect anything wrong");
        } catch (IllegalStateException expected) {
        }
        assertEquals(0, gate().getNumberOfEmailsSent());
    }
}
