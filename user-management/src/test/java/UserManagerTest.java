import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class UserManagerTest extends TestCase {

    UserManager usermanager = (UserManager) IUserManager.INSTANCE;

    @Test
    public void testChangePassWorks() {
        boolean results = usermanager.changePassword("layla@gmail.com", "pass", "pass1");
        assertTrue(results);
    }

    @Test
    public void testChangePassWithWrongPass() {
        boolean results = usermanager.changePassword("layla@gmail.com", "wrong_old_pass", "pass");
        assertFalse(results);
    }

    @Test
    public void testAddUserWorks() {
        //uses email gateway stub
        boolean results = usermanager.signup("layla1@gmail.com", "name", "pass");
        assertTrue(results);
    }

    @Test
    public void testAddExistingUser() {
        boolean results = usermanager.changePassword("layla@gmail.com", "wrong_old_pass", "pass");
        assertFalse(results);
    }

    @Test
    public void testGatewayStub() {
        // before mocking:
        assert(usermanager.gateway.getClass().equals(EmailGatewayStub.class));
        EmailGateway gateway = Mockito.spy(usermanager.gateway);
        usermanager.setGateway(gateway);
        usermanager.forgotPassword("layla@gmail.com");
        verify(gateway).send(eq("layla@gmail.com"), eq("name"), Mockito.any(), eq("forgot_pass"));
    }
}