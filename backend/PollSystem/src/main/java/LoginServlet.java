import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    IUserManager userManager = IUserManager.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String newPassword = request.getParameter("newPassword");

        HttpSession session = request.getSession();
        OutputStream out = response.getOutputStream();
        String result = "";

        //TODO: add another when user forgot pass and is setting a new pass, and they pass a token

        // if only email is not null, user clicked on forgot password
        if (email != null && password == null && name == null && newPassword == null) {
            result = new Gson().toJson(userManager.forgotPassword(email));
        }
        // if email, password, and newPassword is not null, user wishes to change their password
        else if (email != null && password != null && newPassword != null) {
            result = new Gson().toJson(userManager.changePassword(email, password, newPassword));
        }
        // if email, password, and name is not null, user wishes to sign up
        else if (email != null && password != null && name != null) {
            result = new Gson().toJson(userManager.signup(email, password, name));
        }
        // if other fields are null, except for email and pw, user wishes to log in
        else if (email != null && password != null) {
            boolean success = userManager.authenticateUser(email, password);
            result = new Gson().toJson(success);
            if (success) {
                session.setAttribute("email", email);
            } else {
                session.invalidate();
            }
        }
        out.write(result.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
